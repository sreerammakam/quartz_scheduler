package com.lca.eps.common.filter;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Priority;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@PreMatching
@Priority(Integer.MIN_VALUE)
public class RequestResponseLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter, WriterInterceptor {

	static {
		SKIPPED_CONTENT_TYPES = Set.of(MediaType.APPLICATION_OCTET_STREAM,
				"application/zip",
				"application/tar",
				"application/pdf",
				MediaType.TEXT_HTML);
	}

	private final AtomicLong aid = new AtomicLong(0);
	private static final Comparator<Map.Entry<String, List<String>>> COMPARATOR =
			(o1, o2) -> o1.getKey().compareToIgnoreCase(o2.getKey());

	private final int maxEntitySize;
	private final boolean isPrintEntity;
	private final Logger logger;
	private final Set<String> skippedURIs;

	private static final String EPS_REQUEST_UUID = "eps-requestUUID";
	private static final String NOTIFICATION_PREFIX = "* ";
	private static final String REQUEST_PREFIX = "> ";
	private static final String RESPONSE_PREFIX = "< ";
	private static final String ENTITY_LOGGER_PROPERTY = RequestResponseLoggingFilter.class.getName() + ".entityLogger";
	private static final Set<String> SKIPPED_CONTENT_TYPES;
	private static final int DEFAULT_MAX_ENTITY_SIZE = 5 * 1024;

	/**
	 * Create a logging filter logging the request and response to a default JDK
	 * logger, named as the fully qualified class name of this class. Entity
	 * logging is turned off by default.
	 */
	public RequestResponseLoggingFilter() {
		this(LoggerFactory.getLogger(RequestResponseLoggingFilter.class.getName()),
				false,
				DEFAULT_MAX_ENTITY_SIZE,
				Collections.emptySet());
	}

	public RequestResponseLoggingFilter(boolean isPrintEntity) {
		this(LoggerFactory.getLogger(RequestResponseLoggingFilter.class.getName()),
				isPrintEntity,
				DEFAULT_MAX_ENTITY_SIZE,
				Collections.emptySet());
	}

	public RequestResponseLoggingFilter(boolean isPrintEntity, Set<String> skippedURIs) {
		this(LoggerFactory.getLogger(RequestResponseLoggingFilter.class.getName()),
				isPrintEntity,
				DEFAULT_MAX_ENTITY_SIZE,
				skippedURIs);
	}

	/**
	 * Create a logging filter with custom logger and custom settings of entity
	 * logging.
	 *
	 * @param logger        the logger to log requests and responses.
	 * @param isPrintEntity if true, entity will be logged as well up to the default
	 *                      maxEntitySize, which is {@link RequestResponseLoggingFilter#DEFAULT_MAX_ENTITY_SIZE}
	 */
	public RequestResponseLoggingFilter(final Logger logger, final Boolean isPrintEntity) {
		this(logger, isPrintEntity, DEFAULT_MAX_ENTITY_SIZE, new HashSet<>());
	}

	/**
	 * Create a logging filter with custom logger and custom settings of entity logging.
	 *
	 * @param logger        the logger to log requests and responses.
	 * @param isPrintEntity if true, entity will be logged as well up to the default
	 *                      maxEntitySize, which is {@link RequestResponseLoggingFilter#DEFAULT_MAX_ENTITY_SIZE}
	 * @param maxEntitySize maximum number of entity bytes to be logged (and buffered) -
	 *                      if the entity is larger, logging filter will print (and buffer
	 *                      in memory) only the specified number of bytes and print
	 *                      "...more..." string at the end.
	 */
	public RequestResponseLoggingFilter(final Logger logger,
	                                    final Boolean isPrintEntity,
	                                    final int maxEntitySize,
	                                    final Set<String> skippedURIs) {
		this.logger = logger;
		this.isPrintEntity = isPrintEntity;
		this.maxEntitySize = maxEntitySize;
		this.skippedURIs = skippedURIs;
		logger.info("inside the logging filter...");
	}

	@Override
	public void filter(ContainerRequestContext containerRequestContext) throws IOException {
		if (IterableUtils.matchesAny(skippedURIs, path -> containerRequestContext.getUriInfo().getPath().contains(path))) {
			return;
		}
		final long id = aid.incrementAndGet();
		final StringBuilder b = new StringBuilder();

		String reqUUID = getHeaderValue(containerRequestContext, EPS_REQUEST_UUID);
		if (StringUtils.isBlank(reqUUID)) {
			reqUUID = getRandomUUID();
		}
		containerRequestContext.getHeaders().putSingle(EPS_REQUEST_UUID, reqUUID);
		ThreadContext.put(EPS_REQUEST_UUID, reqUUID);

		printRequestLine(b,
				id,
				containerRequestContext.getMethod(),
				containerRequestContext.getUriInfo().getRequestUri());
		printPrefixedHeaders(b, id, REQUEST_PREFIX, containerRequestContext.getHeaders());

		String contentType = containerRequestContext.getHeaderString(HttpHeaders.CONTENT_TYPE);

		StringBuilder requestEntityBuilder = new StringBuilder();

		containerRequestContext.setEntityStream(logInboundEntity(requestEntityBuilder,
				containerRequestContext.getEntityStream()
		));

		if (isPrintEntity && containerRequestContext.hasEntity() && !SKIPPED_CONTENT_TYPES.contains(contentType)) {
			b.append(requestEntityBuilder, 0, Math.min(requestEntityBuilder.length(), maxEntitySize));
		}
		log(b);
	}

	@Override
	public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
		if (IterableUtils.matchesAny(skippedURIs, path -> containerRequestContext.getUriInfo().getPath().contains(path))) {
			return;
		}
		final long id = aid.incrementAndGet();
		final StringBuilder b = new StringBuilder();

		String reqUUID = getHeaderValue(containerRequestContext, EPS_REQUEST_UUID);
		transferHeader(containerRequestContext, containerResponseContext, EPS_REQUEST_UUID, reqUUID);

		printResponseLine(b, id, containerResponseContext.getStatus());
		printPrefixedHeaders(b, id, RESPONSE_PREFIX, containerResponseContext.getStringHeaders());

		String contentType = containerResponseContext.getHeaderString(HttpHeaders.CONTENT_TYPE);

		if (isPrintEntity && containerResponseContext.hasEntity() && !SKIPPED_CONTENT_TYPES.contains(contentType)) {
			final OutputStream stream = new LoggingStream(b, containerResponseContext.getEntityStream());
			containerResponseContext.setEntityStream(stream);
			containerRequestContext.setProperty(ENTITY_LOGGER_PROPERTY, stream);
			// not calling log(b) here - it will be called by the interceptor
		} else {
			log(b);
		}
		ThreadContext.clearAll();
	}

	@Override
	public void aroundWriteTo(WriterInterceptorContext writerInterceptorContext) throws IOException, WebApplicationException {
		final LoggingStream stream = (LoggingStream) writerInterceptorContext.getProperty(ENTITY_LOGGER_PROPERTY);
		writerInterceptorContext.proceed();
		if (stream != null) {
			log(stream.getStringBuilder());
		}
	}

	private static String getHeaderValue(ContainerRequestContext requestContext, String headerName) {
		return requestContext.getHeaderString(headerName);
	}

	private String getRandomUUID() {
		return UUID.randomUUID().toString();
	}

	private void printRequestLine(final StringBuilder builder,
	                              final long id,
	                              final String method,
	                              final URI uri) {
		prefixId(builder, id)
				.append(NOTIFICATION_PREFIX)
				.append("Server has received a request on thread ")
				.append(Thread.currentThread().getName())
				.append("\n");

		prefixId(builder, id).
				append(REQUEST_PREFIX)
				.append(method)
				.append(" ")
				.append(uri.toASCIIString())
				.append("\n");
	}

	private void printResponseLine(final StringBuilder builder,
	                               final long id,
	                               final int status) {
		prefixId(builder, id)
				.append(NOTIFICATION_PREFIX)
				.append("Server responded with a response on thread ")
				.append(Thread.currentThread().getName())
				.append("\n");

		prefixId(builder, id)
				.append(RESPONSE_PREFIX)
				.append(status)
				.append("\n");
	}

	private StringBuilder prefixId(final StringBuilder builder, final long id) {
		return builder.append(id).append(" ");
	}

	private void printPrefixedHeaders(StringBuilder b, long id, String prefix, MultivaluedMap<String, String> headers) {
		for (final Map.Entry<String, List<String>> headerEntry : getSortedHeaders(headers.entrySet())) {
			final List<?> val = headerEntry.getValue();
			final String header = headerEntry.getKey();

			if (val.size() == 1) {
				prefixId(b, id).append(prefix).append(header).append(": ").append(val.get(0)).append("\n");
			} else {
				final StringBuilder sb = new StringBuilder();
				boolean add = false;
				for (final Object s : val) {
					if (add) {
						sb.append(',');
					}
					add = true;
					sb.append(s);
				}
				prefixId(b, id).append(prefix).append(header).append(": ").append(sb.toString()).append("\n");
			}
		}
	}

	private Set<Map.Entry<String, List<String>>> getSortedHeaders(final Set<Map.Entry<String, List<String>>> headers) {
		final TreeSet<Map.Entry<String, List<String>>> sortedHeaders = new TreeSet<>(COMPARATOR);
		sortedHeaders.addAll(headers);
		return sortedHeaders;
	}

	private InputStream logInboundEntity(final StringBuilder b, InputStream stream) throws IOException {
		if (!stream.markSupported()) {
			stream = new BufferedInputStream(stream);
		}
		stream.mark(RequestResponseLoggingFilter.DEFAULT_MAX_ENTITY_SIZE + 1);
		final byte[] entity = new byte[RequestResponseLoggingFilter.DEFAULT_MAX_ENTITY_SIZE + 1];
		final int entitySize = Math.max(0, stream.read(entity));
		b.append(new String(entity, 0, Math.min(entitySize, RequestResponseLoggingFilter.DEFAULT_MAX_ENTITY_SIZE)));
		if (entitySize > RequestResponseLoggingFilter.DEFAULT_MAX_ENTITY_SIZE) {
			b.append("...more...");
		}
		b.append('\n');
		stream.reset();
		return stream;
	}

	private void log(final StringBuilder b) {
		if (logger != null && logger.isDebugEnabled())
			logger.debug(b.toString());
	}

	public static void transferHeader(ContainerRequestContext requestContext, ContainerResponseContext responseContext, String headerName, String defaultValue) {

		String headerValue = getHeaderValue(requestContext, headerName);
		if (StringUtils.isBlank(headerValue)) {
			headerValue = defaultValue;
		}
		responseContext.getHeaders().putSingle(headerName, headerValue);
	}

	private class LoggingStream extends OutputStream {
		private final StringBuilder b;

		private final OutputStream inner;

		private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		LoggingStream(final StringBuilder b, final OutputStream inner) {
			this.b = b;
			this.inner = inner;
		}

		StringBuilder getStringBuilder() {
			// write entity to the builder
			final byte[] entity = baos.toByteArray();

			b.append(new String(entity, 0, Math.min(entity.length, maxEntitySize)));
			if (entity.length > maxEntitySize) {
				b.append("...more...");
			}
			b.append('\n');

			return b;
		}

		@Override
		public void write(final int i) throws IOException {
			if (baos.size() <= maxEntitySize) {
				baos.write(i);
			}
			inner.write(i);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			Objects.checkFromIndexSize(off, len, b.length);
			// len == 0 condition implicitly handled by loop bounds
			for (int i = 0; i < len; i++) {
				write(b[off + i]);
			}
		}
	}
}
