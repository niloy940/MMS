package com.niloy.mms.controller;

import com.niloy.mms.HibernateSingleton;
import com.niloy.mms.model.*;
import com.niloy.mms.model.accounts.Account;
import com.sun.xml.internal.bind.v2.model.util.ArrayInfoUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.hibernate.*;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Controller implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Account> accountListView;

    @FXML
    private TextField numberField;

    @FXML
    private TextField balanceField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField middleNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField streetField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField postalField;

    @FXML
    private DatePicker transactionDatePicker;

    @FXML
    private TextField transactionTimeField;

    @FXML
    private ComboBox<Account> accountComboBox;

    @FXML
    private TextField searchFieldTransaction;

    @FXML
    private ComboBox<TransactionType> transactionTypeComboBox;

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker mealDatePicker;

    @FXML
    private TextField mealTimeField;

    @FXML
    private ComboBox<Account> accountComboBoxMeal;

    @FXML
    private TextField searchFieldMeal;

    @FXML
    private TextField mealAmount;

    @FXML
    private ComboBox<Account> accountComboBoxHistory;

    @FXML
    private TextField searchFieldHistory;

    @FXML
    private TableView<Transactions> tableView;

    @FXML
    private TableColumn<Transactions, Integer> accountNumberColumn;

    @FXML
    private TableColumn<Transactions, Name> nameColumn;

    @FXML
    private TableColumn<Transactions, TransactionType> transactionTypeColumn;

    @FXML
    private TableColumn<Transactions, LocalDate> dateColumn;

    @FXML
    private TableColumn<Transactions, LocalTime> timeColumn;

    @FXML
    private TableColumn<Transactions, Double> amountColumn;

    @FXML
    private TextField depositeTextField;

    @FXML
    private TextField totalmealTextField;

    @FXML
    private TextField withdradTextField;

    @FXML
    private TextField mealRateTextField;

    @FXML
    private TextField extraTextField;

    @FXML
    private ComboBox<Account> accountComboBoxCalc;

    @FXML
    private Label depositeLable;

    @FXML
    private Label mealLable;

    @FXML
    private Label costLable;

    @FXML
    private Label getLable;

    @FXML
    private Label payLable;

    private ObservableList<Account> accountObservableList;
    private ObservableList<Account> filteredAccountObservableList;
    private ObservableList<TransactionType> transactionTypeList;
    private ObservableList<Transactions> transactionList;
    private Account selectedAccount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accountObservableList = FXCollections.observableArrayList();
        filteredAccountObservableList = FXCollections.observableArrayList();
        transactionTypeList = FXCollections.observableArrayList();
        transactionList = FXCollections.observableArrayList();

        accountListView.setItems(filteredAccountObservableList);
        accountComboBox.setItems(filteredAccountObservableList);
        accountComboBoxMeal.setItems(filteredAccountObservableList);
        accountComboBoxHistory.setItems(filteredAccountObservableList);
        accountComboBoxCalc.setItems(filteredAccountObservableList);
        tableView.setItems(transactionList);

        transactionTypeList.addAll(TransactionType.values());
        transactionTypeComboBox.setItems(transactionTypeList);

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        transactionDatePicker.setValue(currentDate);
        mealDatePicker.setValue(currentDate);
        transactionTimeField.setText(currentTime.toString());
        mealTimeField.setText(currentTime.toString());


        //Fetching data from database
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transactionHibernate = null;
        try {
            transactionHibernate = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
            criteriaQuery.from(Account.class);

            List<Account> accountList = session.createQuery(criteriaQuery).list();

            accountObservableList.addAll(accountList);
            filteredAccountObservableList.addAll(accountList);
        } catch (HibernateException he) {
            transactionHibernate.rollback();
            System.err.println(he);
        } finally {
            session.close();
        }
    }

    @FXML
    void handleAddAction(ActionEvent event) {

    }

    @FXML
    void handleCreateAction(ActionEvent event) {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            int accountNumber = Integer.parseInt(numberField.getText());
            String firstName = firstNameField.getText();
            String middleName = middleNameField.getText();
            String lastName = lastNameField.getText();
            String streetAddress = streetField.getText();
            String city = cityField.getText();
            String country = countryField.getText();
            String postalCode = postalField.getText();
            double balance = Double.parseDouble(balanceField.getText());

            Name name = new Name(firstName, lastName, middleName);

            Address address = new Address(streetAddress, city, country, postalCode);

            Account account = new Account(accountNumber, name, address, balance);

            accountObservableList.add(account);
            filteredAccountObservableList.add(account);

            session.save(name);
            session.save(account);

            transaction.commit();
            reset();

            informationAlert("Congratulation!", "Account has been created!");
        } catch (HibernateException he) {
            System.err.println("Duplicate entry!");
            transaction.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    @FXML
    void handleDeleteAction(ActionEvent event) {

    }

    @FXML
    void handleFilterAction(ActionEvent event) {
        filter(searchField.getText().toLowerCase());
    }

    @FXML
    void handleHistoryAccountClickAction(MouseEvent event) {
        filteredAccountObservableList.sort(Comparator.comparing(Account::getAccountNumber));
    }

    @FXML
    void handleHistoryFilterAction(ActionEvent event) {
        filter(searchFieldHistory.getText().toLowerCase());
    }

    @FXML
    void handleHistoryKeyFilterAction(KeyEvent event) {
        filter(searchFieldHistory.getText().toLowerCase());
    }

    @FXML
    void handleHistoryResetAction(ActionEvent event) {
        reset();
    }

    @FXML
    void handleHistorySubmitAction(ActionEvent event) {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transactionHibernate = null;
        try {
            transactionHibernate = session.beginTransaction();

            Account account = accountComboBoxHistory.getSelectionModel().getSelectedItem();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Transactions> criteriaQuery = criteriaBuilder.createQuery(Transactions.class);
            criteriaQuery.from(Transactions.class);

            List<Transactions> transactions = session.createQuery(criteriaQuery).list();

            transactions.forEach(action -> {
                if (account.getAccountNumber() == action.getAccountNumber()) {
                    transactionList.add(action);
                }
                accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
                timeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionTime"));
                amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
            });

        } catch (HibernateException he) {
            transactionHibernate.rollback();
            System.err.println(he);
        } finally {
            session.close();
        }
        searchFieldHistory.clear();
    }

    @FXML
    void handleKeyFilterAction(KeyEvent event) {
        filter(searchField.getText().toLowerCase());
    }

    @FXML
    void handleListClickAction(MouseEvent event) {
        selectedAccount = accountListView.getSelectionModel().getSelectedItem();
        displayData();
    }

    @FXML
    void handleResetAction(ActionEvent event) {
        reset();
    }

    @FXML
    void handleSaveAction(ActionEvent event) {

    }

    @FXML
    void handleSubmitAction(ActionEvent event) {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transactionHibernate = null;
        try {
            transactionHibernate = session.beginTransaction();

            Account account = accountComboBox.getSelectionModel().getSelectedItem();
            TransactionType transactionType = transactionTypeComboBox.getSelectionModel().getSelectedItem();
            LocalDate transactionDate = transactionDatePicker.getValue();
            LocalTime transactionTime = LocalTime.parse(transactionTimeField.getText());
            int amount = Integer.parseInt(amountField.getText());

            Transactions transactions = new Transactions(account, transactionType, transactionDate, transactionTime, amount);

            double oldBalance = account.getBalance();

            if (transactionType == TransactionType.DEPOSITE) {
                account.deposit(amount);
                informationAlert("Transaction", "Deposite Successful!\n\nOld Balance : " + oldBalance + "\nDeposited Amount : " + amount + "\nNew Balance : " + account.getBalance());
            } else {
                account.withdraw(amount);
                informationAlert("Transaction", "Withdraw Sucsessful!\n\nOld Balance : " + oldBalance + "\nWithdrawal Amount : " + amount + "\nNew Balance : " + account.getBalance());
            }
            session.update(account);
            session.save(transactions);
            transactionHibernate.commit();

        } catch (HibernateException he) {
            transactionHibernate.rollback();
            System.err.println(he);
        } finally {
            session.close();
        }
    }

    @FXML
    void handleTransactionAccountClickAction(MouseEvent event) {
        filteredAccountObservableList.sort(Comparator.comparing(Account::getAccountNumber));
    }

    @FXML
    void handleTransactionFilterAction(ActionEvent event) {
        filter(searchFieldTransaction.getText().toLowerCase());
    }

    @FXML
    void handleTransactionKeyFilterAction(KeyEvent event) {
        filter(searchFieldTransaction.getText().toLowerCase());
    }

    @FXML
    void handleSubmitActionMeal(ActionEvent event) {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transactionHibernate = null;
        try {
            transactionHibernate = session.beginTransaction();

            Account account = accountComboBoxMeal.getSelectionModel().getSelectedItem();
            LocalDate mealDate = mealDatePicker.getValue();
            LocalTime mealTime = LocalTime.parse(mealTimeField.getText());
            int amount = Integer.parseInt(mealAmount.getText());

            Meal meal = new Meal(account, mealDate, mealTime, amount);
            informationAlert("Done", "Meal added successfully!");

            session.save(meal);
            transactionHibernate.commit();

        } catch (HibernateException he) {
            transactionHibernate.rollback();
            System.err.println(he);
        }
    }

    @FXML
    void handleMealAccountClickAction(MouseEvent event) {
        filteredAccountObservableList.sort(Comparator.comparing(Account::getAccountNumber));
    }

    @FXML
    void handleMealFilterAction(ActionEvent event) {
        filter(searchFieldMeal.getText().toLowerCase());
    }

    @FXML
    void handleMealKeyFilterAction(KeyEvent event) {
        filter(searchFieldMeal.getText().toLowerCase());
    }


    @FXML
    void handleCalcAccountClickAction(MouseEvent event) {
        filteredAccountObservableList.sort(Comparator.comparing(Account::getAccountNumber));
    }

    @FXML
    void handleCalcShowAction(ActionEvent event) {
        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transactionHibernate = null;
        try {
            transactionHibernate = session.beginTransaction();

            //total deposit
            String hqlDeposit = "select amount from Transactions where transactionType=0";
            Query queryDeposit = session.createQuery(hqlDeposit);
            List<Double> depositAmount = ((org.hibernate.query.Query) queryDeposit).list();

            double sumDeposit = depositAmount.stream()
                    .mapToDouble(a -> a)
                    .sum();

            depositeTextField.setText(sumDeposit + "");

            //total withdraw
            String hqlWithdraw = "select amount from Transactions where transactionType=1";
            Query queryWithdraw = session.createQuery(hqlWithdraw);
            List<Double> withdrawAmount = ((org.hibernate.query.Query) queryWithdraw).list();

            double sumWithdraw = withdrawAmount
                    .stream()
                    .mapToDouble(a -> a)
                    .sum();
            withdradTextField.setText(sumWithdraw + "");

            //extra
            double extra = sumDeposit - sumWithdraw;
            extraTextField.setText(extra + "");

            //total meal
            String hqlMeal = "select amount from Meal";
            Query queryMeal = session.createQuery(hqlMeal);
            List<Integer> mealAmount = ((org.hibernate.query.Query) queryMeal).list();

            int sumMeal = mealAmount
                    .stream()
                    .mapToInt(a -> a)
                    .sum();
            totalmealTextField.setText(sumMeal + "");

            //meal rate
            double mealRate = sumWithdraw / sumMeal;
            mealRateTextField.setText(mealRate + "");

            transactionHibernate.commit();
        } catch (HibernateException he) {
            transactionHibernate.rollback();
            System.err.println(he);
        } finally {
            session.close();
        }
    }

    @FXML
    void handleCalcSubmitAction(ActionEvent event) {
        selectedAccount = accountComboBoxCalc.getSelectionModel().getSelectedItem();

        SessionFactory sessionFactory = HibernateSingleton.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transactionHibernate = null;
        try {
            transactionHibernate = session.beginTransaction();

            //deposit
            String hqlSD = "select amount from Transactions where transactionType=0 and account.accountNumber = :id";
            Query querySD = session.createQuery(hqlSD);
            querySD.setParameter("id", selectedAccount.getAccountNumber());

            List<Double> sdList = ((org.hibernate.query.Query) querySD).list();

            double sumSD = sdList
                    .stream()
                    .mapToDouble(a -> a)
                    .sum();
            depositeLable.setText(sumSD + "");

            //meal
            String hqlSM = "select amount from Meal where account.accountNumber = :id";
            Query querySM = session.createQuery(hqlSM);
            querySM.setParameter("id", selectedAccount.getAccountNumber());

            List<Integer> smList = ((org.hibernate.query.Query) querySM).list();

            int sumSM = smList
                    .stream()
                    .mapToInt(a -> a)
                    .sum();
            mealLable.setText(sumSM + "");

            //cost

            //totalWithdraw
            String hqlWithdraw = "select amount from Transactions where transactionType=1";
            Query queryWithdraw = session.createQuery(hqlWithdraw);
            List<Double> withdrawAmount = ((org.hibernate.query.Query) queryWithdraw).list();

            double sumWithdraw = withdrawAmount
                    .stream()
                    .mapToDouble(a -> a)
                    .sum();

            //total meal
            String hqlMeal = "select amount from Meal";
            Query queryMeal = session.createQuery(hqlMeal);
            List<Integer> mealAmount = ((org.hibernate.query.Query) queryMeal).list();

            int sumMeal = mealAmount
                    .stream()
                    .mapToInt(a -> a)
                    .sum();

            double mealRate = sumWithdraw / sumMeal;

            double cost = sumSM * mealRate;
            costLable.setText(cost + "");

            //Get
            selectedAccount = accountComboBoxCalc.getSelectionModel().getSelectedItem();
            if (sumSD > cost){
                double get = sumSD - cost;
                getLable.setText(get + "");
            }
            if (cost > sumSD){
                double pay = cost - sumSD;
                payLable.setText(pay + "");
            }

            transactionHibernate.commit();

        } catch (HibernateException he) {
            transactionHibernate.rollback();
            System.err.println(he);
        }
    }

    private void clearForm() {
        numberField.clear();
        firstNameField.clear();
        middleNameField.clear();
        lastNameField.clear();
        streetField.clear();
        cityField.clear();
        countryField.clear();
        postalField.clear();
        balanceField.clear();
        amountField.clear();
        mealAmount.clear();
        searchField.clear();
        searchFieldTransaction.clear();
        searchFieldHistory.clear();
        searchFieldMeal.clear();
        accountComboBox.valueProperty().set(null);
        accountComboBoxHistory.valueProperty().set(null);
        accountComboBoxMeal.valueProperty().set(null);
        transactionTypeComboBox.valueProperty().set(null);
    }

    private void reset() {
        clearForm();
        tableView.getItems().clear();
        filteredAccountObservableList.sort(Comparator.comparing(Account::getAccountNumber));

        if (filteredAccountObservableList.isEmpty()) {
            numberField.setText("1");
        } else {
            Account ac = filteredAccountObservableList.get(filteredAccountObservableList.size() - 1);
            numberField.setText(ac.getAccountNumber() + 1 + "");
        }
    }

    private void displayData() {
        numberField.setText(selectedAccount.getAccountNumber() + "");
        firstNameField.setText(selectedAccount.getName().getFirstName());
        middleNameField.setText(selectedAccount.getName().getMiddleName());
        lastNameField.setText(selectedAccount.getName().getLastName());
        streetField.setText(selectedAccount.getAddress().getStreetAddress());
        cityField.setText(selectedAccount.getAddress().getCity());
        countryField.setText(selectedAccount.getAddress().getCountry());
        postalField.setText(selectedAccount.getAddress().getPostalCode());
        balanceField.setText(selectedAccount.getBalance() + "");
    }

    private void filter(String name) {
        filteredAccountObservableList.clear();

        accountObservableList.forEach(action -> {
            Account account = action;
            if (account.getName().toString().toLowerCase().contains(name)) {
                filteredAccountObservableList.add(account);
            }
        });
    }

    private void informationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            reset();
        }
    }
}
