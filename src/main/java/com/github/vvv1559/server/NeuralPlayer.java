package com.github.vvv1559.server;


import com.github.vvv1559.network.NeuralEvolution;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NeuralPlayer extends HttpServlet {
    private enum Action {
        RESET("/reset"),
        NEXT_GEN("/nextGen"),
        DIED("/died"),
        NEED_FLAP("/needFlap");

        private static final Map<String, Action> actionsMap;

        static {
            actionsMap = Stream.of(Action.values()).collect(Collectors.toMap(Action::getPath, Function.identity()));
        }

        private static Action fromPath(String path) {
            return actionsMap.get(path);
        }


        private final String path;

        Action(String path) {
            this.path = "/play" + path;
        }

        private String getPath() {
            return path;
        }

    }

    private static final Gson GSON = new Gson();
    private static final Type REQUEST_LIST_TYPE = new TypeToken<HashMap<Integer, double[]>>() {
    }.getType();


    private NeuralEvolution neuralEvolution;

    public NeuralPlayer() {
        neuralEvolution = new NeuralEvolution();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        switch (Action.fromPath(req.getRequestURI())) {
            case RESET:
                neuralEvolution = new NeuralEvolution();
                break;

            case NEXT_GEN:
                resp.getWriter().append(Integer.toString(neuralEvolution.nextGeneration()));
                break;

            case DIED:
                int index = Integer.parseInt(req.getParameter("index"));
                int score = Integer.parseInt(req.getParameter("score"));
                neuralEvolution.fixScore(index, score);

            default:
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        switch (Action.fromPath(req.getRequestURI())) {
            case NEED_FLAP:
                Map<Integer, double[]> requestInfo = GSON.fromJson(req.getReader(), REQUEST_LIST_TYPE);

                Map<Integer, Double> r = new HashMap<>();
                for (Map.Entry<Integer, double[]> entry : requestInfo.entrySet()) {
                    double[] result = neuralEvolution.compute(entry.getKey(), entry.getValue());
                    r.put(entry.getKey(), result[0]);
                }

                resp.getWriter().append(GSON.toJson(r));
                break;

            default:
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
