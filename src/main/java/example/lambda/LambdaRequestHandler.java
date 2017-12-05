package example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import example.controllers.MainController;
import example.models.User;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Map;


@SpringBootApplication
public class LambdaRequestHandler {

  public LambdaResponse handleRequest(LambdaRequest request, Context context) {

    LambdaLogger lambdaLogger = context.getLogger();
    String[] args = new String[0];

    MainController mainController = new MainController();
    ObjectMapper mapper;

    switch (request.getPath()) {
      case "/demo/add":
        if (request.getHttpMethod().equalsIgnoreCase("GET")) {
          Map<String, String> queryParams = request.getQueryStringParameters();
          String name = queryParams.getOrDefault("name", null);
          String email = queryParams.getOrDefault("email", null);
          System.out.println("name::" + name);
          System.out.println("email::" + email);
          String userCreated = mainController.addNewUser(name, email);
          return new LambdaResponse(200, null, "{\"success\":\"" + userCreated + "\"}");
        } else {
          return new LambdaResponse(400, null, "{\"message\":\"HTTP method not found\"}");
        }

      case "/demo/all":
        Iterable<User> users = mainController.getAllUsers();
        mapper = new ObjectMapper();
        try {
          String usersResponse = mapper.writeValueAsString(users);

          return new LambdaResponse(200, null, usersResponse);
        } catch (IOException e) {
          lambdaLogger.log("Exception::" + e.getMessage());
          lambdaLogger.log(e.toString());
          return new LambdaResponse(500, null, null);
        }

      default:
        return new LambdaResponse(400, null, "{\"message\":\"No matching route\"}");
    }

  }

}
