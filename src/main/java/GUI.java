/**
 * Currency Converter (with API usage) by Dominic Philipp
 * 21.03.2023
 * Currently only for few specific currencies
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import POJO.Exchange;
import okhttp3.*;

import java.io.*;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GUI extends JFrame {
    private JPanel panelMain;
    private JButton convertButton;
    private JTextField txtAmount;
    private JComboBox comboBoxFrom;
    private JComboBox comboBoxTo;
    private JLabel lblPrevAmount;
    private JLabel lblConvertedAmount;
    private JLabel lblFromToRatio;
    private JLabel lblToFromRatio;
    private JLabel lblError;
    private JLabel lblWarning;

    //OkHttpClient Version
    OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.MINUTES)    // connect timeout
            .writeTimeout(5, TimeUnit.MINUTES)      // write timeout
            .readTimeout(5, TimeUnit.MINUTES)       // read timeout
            .build();

    // creating Objectmapper
    ObjectMapper mapper = new ObjectMapper();

    public GUI() {

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // checks if the userinput only consists of numbers
                    if (txtAmount.getText().matches("^[0-9]+(.[0-9]+)?$")) {

                        // empties error-label incase the previous userinput was wrong
                        lblError.setText(" ");

                        // build Request with userinput per GUI
                        Request request = new Request.Builder()
                                .url("https://api.apilayer.com/exchangerates_data/convert?to="
                                        + comboBoxTo.getSelectedItem().toString()
                                        + "&from=" + comboBoxFrom.getSelectedItem().toString()
                                        + "&amount=" + txtAmount.getText())
                                .addHeader("apikey", "mk9dx6LTRzWAVf93BgJpjSHIpCGgVymd")
                                .build();
                        Response response = client.newCall(request).execute();

                        // read response body into Exchange Object
                        Exchange exchangeResponse = mapper.readValue(response.body().string(), Exchange.class);

                        // Display the chosen amount from the User again -> Example: 10 USD
                        lblPrevAmount.setText(exchangeResponse.getQuery().getAmount()
                                + " " + exchangeResponse.getQuery().getFrom() + " =");

                        // Convert the variable result (double) into a String
                        String convertedAmountAsString = exchangeResponse.getResult() + "";

                        // fill lblConvertAmount with String results and the chosen Currency -> Example 9.0123 EUR
                        lblConvertedAmount.setText(convertedAmountAsString
                                + " " + exchangeResponse.getQuery().getTo());

                        // Show exchange rates for both currencies -> Example 1 USD = 1.123 EUR

                        lblFromToRatio.setText("1 " + exchangeResponse.getQuery().getFrom()
                                + " = " + (1 * exchangeResponse.getInfo().getRate())
                                + " " + exchangeResponse.getQuery().getTo());

                        lblToFromRatio.setText("1 " + exchangeResponse.getQuery().getTo()
                                + " = " + (1 / exchangeResponse.getInfo().getRate())
                                + " " + exchangeResponse.getQuery().getFrom());

                        response.close();
                    } else {
                        // notifies user that the input was wrong
                        lblError.setText("The amount has to be a number!");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }

    public static void main(String[] args) throws IOException {
        GUI window = new GUI();

        // UI window specifications
        window.setContentPane(window.panelMain);
        window.setTitle("Currency Converter");
        window.setSize(600, 400);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // blocks the user from pasting into the textfield to prevent errors
        window.txtAmount.setTransferHandler(null);

    }

}

