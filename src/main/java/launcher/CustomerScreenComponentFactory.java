package launcher;

import javafx.stage.Stage;
import model.User;
import view.CustomerView;

public class CustomerScreenComponentFactory {
    public CustomerView customerView;
    private static volatile CustomerScreenComponentFactory instance;

    public static CustomerScreenComponentFactory getInstance(Stage primaryStage){
        if (instance == null){
            synchronized (CustomerScreenComponentFactory.class) {
                if (instance == null)
                    instance = new CustomerScreenComponentFactory(primaryStage);
            }
        }

        return instance;

    }

    private CustomerScreenComponentFactory(Stage primaryStage) {
        customerView = new CustomerView(primaryStage);
    }

    public CustomerView getCustomerView(){
        return customerView;
    }
}
