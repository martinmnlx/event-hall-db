package com.infom.eventhall;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import com.infom.eventhall.dao.*;
import com.infom.eventhall.ui.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppFrame());
    }
}
