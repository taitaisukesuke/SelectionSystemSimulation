import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ChartApplication extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Selection System Simulation");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Times");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setCreateSymbols(true);
        lineChart.setTitle("Average of Score");


        //defining a series
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        series1.setName("選抜");

        series2.setName("分散");

        //populating the series with data


        Scene scene = new Scene(lineChart, 1000, 800);
        lineChart.getData().add(series1);
        lineChart.getData().add(series2);


        Task<Boolean> task1 = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                Main main1 = new Main(Setting.AGENT_GROUP_NUM, Setting.AGENT_NUM_IN_ONE_GROUP, Setting.BETA, Setting.GYOUMU_NUM, Setting.PERCENTAGE_OF_LEANING, Setting.SYUYAKUDO,true, new Main.EvaluateListener() {
                    @Override
                    public void onEvaluated(double average) {
                        Platform.runLater(() -> {
                            XYChart.Data newData = new XYChart.Data(lineChart.getData().get(0).getData().size(), average);

                            lineChart.getData().get(0).getData().add(newData);
                            newData.getNode().setStyle("-fx-stroke: red;-fx-background-color:red;");
                            lineChart.getData().get(0).getNode().setStyle("-fx-stroke: red;-fx-background-color:red;");

                            System.out.println("add");
                        });
                    }
                });

                Main.runMain(main1);
                return true;
            }

            ;
        };


        Task<Boolean> task2 = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                Main main2 = new Main(Setting.AGENT_GROUP_NUM, Setting.AGENT_NUM_IN_ONE_GROUP, Setting.BETA, Setting.GYOUMU_NUM, Setting.PERCENTAGE_OF_LEANING, Setting.SYUYAKUDO,false, new Main.EvaluateListener() {
                    @Override
                    public void onEvaluated(double average) {
                        Platform.runLater(() -> {
                            XYChart.Data newData = new XYChart.Data(lineChart.getData().get(1).getData().size(), average);

                            lineChart.getData().get(1).getData().add(newData);
                            newData.getNode().setStyle("-fx-stroke: blue;-fx-background-color:blue;");
                            lineChart.getData().get(1).getNode().setStyle("-fx-stroke: blue;-fx-background-color:blue;");
                            System.out.println("add");
                        });
                    }
                });
                Main.runMain(main2);
                return true;
            }
        };


        Thread t = new Thread(task1);
        t.setDaemon(true);
        t.start();
        Thread t2 = new Thread(task2);
        t2.setDaemon(true);
        t2.start();

        scene.getStylesheets().add("./stylesheet.css");
        stage.setScene(scene);

        stage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
