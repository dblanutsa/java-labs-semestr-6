package blanutsa.dmitriy.servlets;import blanutsa.dmitriy.controller.CarController;import blanutsa.dmitriy.controller.Controller;import blanutsa.dmitriy.controller.ListRequest;import blanutsa.dmitriy.controller.TableResponse;import blanutsa.dmitriy.controller.dto.CarDto;import blanutsa.dmitriy.view.View;import javax.servlet.annotation.WebServlet;import javax.servlet.http.HttpServlet;import javax.servlet.http.HttpServletRequest;import javax.servlet.http.HttpServletResponse;@WebServlet(urlPatterns = "/car/list")public class CarListServlet extends HttpServlet {    private final Controller<CarDto, String> controller = new CarController();    private final View view = new View();    @Override    protected void doPost(HttpServletRequest request, HttpServletResponse response) {        TableResponse<CarDto> tableResponse = controller.getList((ListRequest) request.getAttribute("request"));        view.setAttribute(request, "response", tableResponse);        view.forward(request, response, "carList.jsp");    }}