package org.example.controller;

import io.swagger.annotations.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.models.Audio;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@SwaggerDefinition(
        info = @Info(
                description = "Audio server",
                version = "1.0.0",
                title = "Audio Servlet"
        ),
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        tags = {@Tag(name = "audio", description = "Operations about audio songs")}
)
@Api(value = "/audio", description = "gets some data from a servlet")
@WebServlet(name = "AudioServlet", value = "AudioServlet")
public class AudioController extends HttpServlet {
    public List<Audio> list = new ArrayList<Audio>();

    @ApiOperation(httpMethod = "GET", value = "Resource to get a audio list", response = Audio[].class, nickname = "getAudio")
    @ApiResponses({@ApiResponse(code = 400, message = "Invalid input", response = Audio[].class)})
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(list);
        out.println(json);
        out.close();
    }

    @ApiOperation(httpMethod = "POST", value = "Resource to add to audio list", response = Audio.class, nickname = "createAudio")
    @ApiResponses({@ApiResponse(code = 400, message = "Invalid input", response = Audio.class)})
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        Audio audio = mapper.readValue(request.getInputStream(), Audio.class);
        this.list.add(audio);
        String json = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(list);
        out.println(json);
        out.close();
    }
}
