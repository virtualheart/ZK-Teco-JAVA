package com.zkteco.iclockhelper;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ZKTecoHttpServer {

    public ZKTecoHttpServer(int port) throws IOException, URISyntaxException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Context for /iclock/cdata.aspx (GET and POST)
        server.createContext("/iclock/cdata.aspx", new ZKTecoHandler());

        // Context for /iclock/getrequest.aspx (GET)
        server.createContext("/iclock/getrequest.aspx", new ZKTecoHandler());
        server.start();

        System.out.println("Server started on port " + port);
    }

    static class ZKTecoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            String requestUri = exchange.getRequestURI().toString();
            String query = exchange.getRequestURI().getQuery();

            System.out.println("Received " + requestMethod + " request: " + requestUri);
            System.out.println("Query parameters: " + query);

            // Process the request based on the URI
            if (requestUri.startsWith("/iclock/cdata.aspx")) {
                // Handle cdata.aspx requests (GET and POST)
//                handleCDataRequest(exchange);
            	try {
            		CdataRequest us = CdataRequest.fromReq(exchange);
					System.out.println("te" + us.getOperationLog().getUsers());    		
            
                    sendResponse(exchange, "OK");
				} catch (Exception e) {
					e.printStackTrace();
				}
            } else if (requestUri.startsWith("/iclock/getrequest.aspx")) {
                // Handle getrequest.aspx requests (GET)
                handleGetRequest(exchange);
            }
        }

        private void handleCDataRequest(HttpExchange exchange) throws IOException {
            // Get the request method (e.g., GET or POST)
            String requestMethod = exchange.getRequestMethod();

            // Get the query parameters from the URI
            String query = exchange.getRequestURI().getQuery();

            // Get the request body for POST requests
            String requestBody = "";
            if ("POST".equals(requestMethod)) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
                    requestBody = br.readLine();
                    System.out.println("Request body: " + requestBody);
                }
            }

            sendResponse(exchange, "OK");
        }

        private void handleGetRequest(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();

            sendResponse(exchange, "OK");
        }

        private List<String> processCDataRequest(String query, String requestBody) {
            // Add your business logic here to process the request
            // and return a list dynamically based on the query and requestBody

            List<String> resultList = new ArrayList<>();
            resultList.add("Result1");
            resultList.add("Result2");
            resultList.add("Result3");

            return resultList;
        }

        private void sendResponse(HttpExchange exchange, String response) throws IOException {
            // Send the response back to the device
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
