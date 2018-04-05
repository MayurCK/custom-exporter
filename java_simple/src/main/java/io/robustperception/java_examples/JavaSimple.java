package io.robustperception.java_examples;

import io.prometheus.client.Counter;
import io.prometheus.client.exporter.MetricsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JavaSimple {
    static final Counter queuedRequests = Counter.build()
            .name("queued_requests").help("Total queued requests.").register();

    static class ExampleServlet extends HttpServlet {
        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
                throws ServletException, IOException {
            resp.getWriter().println("Hello World!");
            // Increment the number of requests.
            int updateMetrices = Integer.parseInt(req.getParameter("queued_requests"));
            for (int i = 0; i <= updateMetrices; i++) {
                queuedRequests.inc();
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Server server = new Server(7777);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        // Expose our example servlet.
        context.addServlet(new ServletHolder(new ExampleServlet()), "/updatemetrics");
        // Expose Promtheus metrics.
        context.addServlet(new ServletHolder(new MetricsServlet()), "/metrics");
        queuedRequests.inc();
        queuedRequests.inc();
        queuedRequests.inc();

/*
    // Add metrics about CPU, JVM memory etc.
        DefaultExports.initialize();
    //http://35.200.213.215/haproxy_stats
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://35.200.213.215/haproxy_stats");
        HttpResponse response = client.execute(request);
    // Get the response
        BufferedReader rd = new BufferedReader
                (new InputStreamReader(
                        response.getEntity().getContent()));
        StringBuilder textView = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {

            textView.append(line);
        }
        System.out.println(textView.toString());
*/
        // Start the webserver.
        server.start();
        server.join();
    }
}
