/**
 * 
 */
package com.gq.meter.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.gq.meter.object.GoalInput;
import com.gq.meter.util.SqlUtil;
import com.mysql.jdbc.PreparedStatement;

/**
 * @author GQ
 * 
 */
public class GoalInputModel {
    public List<GoalInput> getGoalInput(String goalId) throws Exception {
        Connection dbExchange = null;
        Connection dbCustomer = null;
        PreparedStatement prepareStmt;
        List<GoalInput> goalList = new ArrayList<GoalInput>();
        try {
            dbExchange = SqlUtil.getExchangeConnection();
            // dbCustomer = SqlUtil.getCustomerConnection(entpId);
            String goalInputQuery = "select * from goal_input where goal_id=?;";
            prepareStmt = (PreparedStatement) dbExchange.prepareStatement(goalInputQuery);
            prepareStmt.setString(1, goalId);
            ResultSet goalInputSet = prepareStmt.executeQuery();
            while (goalInputSet.next()) {
                GoalInput inputObj = new GoalInput();
                inputObj.setGoalId(goalInputSet.getString("goal_id"));
                inputObj.setInputId(goalInputSet.getInt("input_id"));
                inputObj.setColHoldr(goalInputSet.getString("col_holdr"));
                inputObj.setDescr(goalInputSet.getString("descr"));
                inputObj.setDtvalue(goalInputSet.getString("dtvalue"));
                goalList.add(inputObj);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return goalList;
    }
}
