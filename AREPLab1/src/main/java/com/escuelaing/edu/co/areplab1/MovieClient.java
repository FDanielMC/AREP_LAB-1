package com.escuelaing.edu.co.areplab1;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor Web para el buscador
 * @author Daniel Fernando Moreno Cerón
 */
public class MovieClient {

    private static final OMDBProvider omdbProvider = new OMDBProvider();

    /**
     * Constructor de la Clase MovieClient 
     */
    public MovieClient() {
    }

    /**
     * Método para iniciar el servidor.
     * 
     * @throws IOException si ocurre un error de entrada/salida
     */
    public static void startServer() throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            boolean readingFirst = true;
            String petition = "";

            while ((inputLine = in.readLine()) != null) {
                if (readingFirst) {
                    petition = inputLine.split(" ")[1];
                    readingFirst = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            outputLine = (petition.startsWith("/movies")) ? moviePage(petition.replace("/movies?name=", "")) : Browser();

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }

        serverSocket.close();
    }

    /**
     * Retorna un HTML con la película que se quiera buscar.
     * 
     * @param name Nombre de la película
     * @return una estructura HTML con información de la película y encabezados
     */
    private static String moviePage(String name) {
        try {
            JsonObject resp = omdbProvider.searchMovie(name);

            String actors = resp.has("Actors") ? resp.get("Actors").getAsString() : "N/A";
            String language = resp.has("Language") ? resp.get("Language").getAsString() : "N/A";

            return "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + "<!DOCTYPE html>\r\n"
                    + "<html>\r\n"
                    + "<head>\r\n"
                    + "    <title>Movie</title>\r\n"
                    + "    <meta charset=\"ISO-8859-1\">\r\n"
                    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                    + "    <link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css2?family=Cinzel:wght@400;700&display=swap\">\r\n"
                    + "    <style>\r\n"
                    + "        body {\r\n"
                    + "            font-family: 'Cinzel', sans-serif;\r\n"
                    + "            margin: 0;\r\n"
                    + "            padding: 0;\r\n"
                    + "        }\r\n"
                    + "        .card {\r\n"
                    + "            box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);\r\n"
                    + "            max-width: 500px;\r\n"
                    + "            margin: 20px auto;\r\n"
                    + "            padding: 20px;\r\n"
                    + "            border-radius: 15px;\r\n"
                    + "            display: flex;\r\n"
                    + "            background: #fff;\r\n"
                    + "            align-items: center;\r\n"
                    + "        }\r\n"
                    + "        .poster {\r\n"
                    + "            width: 150px;\r\n"
                    + "            height: 225px;\r\n"
                    + "            margin-right: 20px;\r\n"
                    + "            border-radius: 15px;\r\n"
                    + "        }\r\n"
                    + "        .details {\r\n"
                    + "            flex: 1;\r\n"
                    + "            display: flex;\r\n"
                    + "            flex-direction: column;\r\n"
                    + "            justify-content: space-between;\r\n"
                    + "        }\r\n"
                    + "        .title {\r\n"
                    + "            font-size: 1.8em;\r\n"
                    + "            margin-bottom: 10px;\r\n"
                    + "            color: #333;\r\n"
                    + "        }\r\n"
                    + "        .info {\r\n"
                    + "            margin-right: 10px;\r\n"
                    + "            display: flex;\r\n"
                    + "            flex-direction: column;\r\n"
                    + "            color: #555;\r\n"
                    + "        }\r\n"
                    + "        .plot {\r\n"
                    + "            font-style: italic;\r\n"
                    + "            margin-bottom: 10px;\r\n"
                    + "            color: #777;\r\n"
                    + "        }\r\n"
                    + "button {\n"
                    + "            background-color: #ff69b4; /* Color rosado */\n"
                    + "            color: #fff;\n"
                    + "            padding: 10px 15px;\n"
                    + "            border: none;\n"
                    + "            border-radius: 5px;\n"
                    + "            text-align: center;\n"
                    + "            text-decoration: none;\n"
                    + "            display: inline-block;\n"
                    + "            font-size: 14px;\n"
                    + "            margin-top: 10px;\n"
                    + "            cursor: pointer;\n"
                    + "        }"
                    + "    </style>\r\n"
                    + "</head>\r\n"
                    + "<body>\r\n"
                    + "    <div class=\"card\">\r\n"
                    + "        <img src=\"" + resp.get("Poster").getAsString() + "\" alt=\"Movie Poster\" class=\"poster\">\r\n"
                    + "        <div class=\"details\">\r\n"
                    + "            <h2 class=\"title\">" + resp.get("Title").getAsString() + "</h2>\r\n"
                    + "            <div class=\"info\">\r\n"
                    + "                <span>Released: " + resp.get("Released") + "</span>\r\n"
                    + "                <span>Genre: " + resp.get("Genre") + "</span>\r\n"
                    + "                <span>Director: " + resp.get("Director") + "</span>\r\n"
                    + "                <span>Actors: " + resp.get("Actors") + "</span>\r\n"
                    + "                <span>Language: " + resp.get("Language") + "</span>\r\n"
                    + "            </div>\r\n"
                    + "            <p class=\"plot\">" + resp.get("Plot") + "</p>\r\n"
                    + "                <a href=\"/\"><button>Limpiar</button></a>\r\n"
                    + "            </article>\r\n"
                    + "        </div>\r\n"
                    + "    </div>\r\n"
                    + "</body>\r\n"
                    + "</html>";
        } catch (Exception e) {
            System.err.println("Error al procesar la película:");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retorna la página HTML del buscador
     * 
     * @return Buscador principal
     */
    private static String Browser() {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type:text/html; charset=utf-8\\r\\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>Filmoteca</title>\n"
                + "    <style>\n"
                + "        body {\n"
                + "            font-family: 'Arial', sans-serif;\n"
                + "            background-color: #f4f4f4;\n"
                + "            margin: 0;\n"
                + "            padding: 0;\n"
                + "            display: flex;\n"
                + "            align-items: center;\n"
                + "            justify-content: center;\n"
                + "            height: 100vh;\n"
                + "        }\n"
                + "\n"
                + "        .container {\n"
                + "            background-color: #fff;\n"
                + "            border-radius: 8px;\n"
                + "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n"
                + "            padding: 20px;\n"
                + "            width: 400px;\n"
                + "            text-align: center;\n"
                + "        }\n"
                + "\n"
                + "        label,\n"
                + "        input,\n"
                + "        button {\n"
                + "            margin-bottom: 15px;\n"
                + "        }\n"
                + "\n"
                + "        input {\n"
                + "            width: 100%;\n"
                + "            padding: 10px;\n"
                + "            box-sizing: border-box;\n"
                + "        }\n"
                + "\n"
                + "        button {\n"
                + "            background-color: #ff69b4; /* Color rosado */\n"
                + "            color: white;\n"
                + "            padding: 10px 15px;\n"
                + "            border: none;\n"
                + "            border-radius: 4px;\n"
                + "            cursor: pointer;\n"
                + "            font-size: 16px;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "\n"
                + "<body>\n"
                + "    <div class=\"container\">\n"
                + "        <h1>Filmoteca</h1>\n"
                + "        <label>¡Busca tu película Favorita!</label>\n"
                + "        <input type=\"text\" id=\"nombre-pelicula\" placeholder=\"Ingrese el nombre de la película\" name=\"name\">\n"
                + "        <button onclick=\"consultMovie()\">Consultar</button>\n"
                + "        <div id=\"pelicula\"></div>\n"
                + "    </div>\n"
                + "\n"
                + "    <script>\n"
                + "        function consultMovie() {\n"
                + "            let nameMovie = document.getElementById(\"nombre-pelicula\").value;\n"
                + "            console.log(nameMovie);\n"
                + "            const xhttp = new XMLHttpRequest();\n"
                + "            xhttp.onload = function () {\n"
                + "                document.getElementById(\"pelicula\").innerHTML =\n"
                + "                    this.responseText;\n"
                + "            }\n"
                + "            xhttp.open(\"GET\", \"/movies?name=\" + nameMovie);\n"
                + "            xhttp.send();\n"
                + "        }\n"
                + "    </script>\n"
                + "</body>\n"
                + "\n"
                + "</html>";
    }
}
