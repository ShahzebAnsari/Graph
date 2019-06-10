package com.example.graph;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TableLayout table;
    TextView formula;
    String[] strFormula=new String[100];
    int index=0;
    String[] stack=new String[100];
    String[] data=new String[]{"log","sin","(",")","%","^","cos","7","8","9","/","tan","4","5","6","*","x","1","2","3","-","ex","pi","0",".","+"};

    public void writeButton(View view){
        int a=Integer.parseInt(((Button)view).getTag().toString());
        String term=data[a];
        formula.setText(formula.getText()+term);
        strFormula[index++]=term;
    }

    public void backButton(View view){
        if(index==0)return;
        index--;
        formula.setText("");
        for(int i=0;i<index;i++)
            formula.setText(formula.getText()+strFormula[i]);
    }

    public void acButton(View view){
        formula.setText("");
        index=0;
    }
    public boolean isClosingParentheses(String s){
        if(s==")")
            return true;
        return false;
    }
    public boolean isOpeningParentheses(String s){
        if(s=="(")
            return true;
        else return false;
    }
    public boolean isOperator(String s){
        if(s=="log"||s=="sin"||s=="cos"||s=="tan"||s=="%"||s=="^"||s=="/"||s=="*"||s=="-"||s=="+")
            return true;
        else
            return false;
    }
    public int operatorPrecedence(String s){
        if(s=="-"||s=="+")
            return 1;
        if(s=="/"||s=="*"||s=="%")
            return 2;
        if(s=="^")
            return 3;
        if(s=="log"||s=="sin"||s=="cos"||s=="tan")
            return 4;
        return 0;
    }
    public void infixToPostfix(){
        int top=-1;
        String[] postfix=new String[2*index+2];
        int j=0;
        strFormula[index]=")";
        index++;
        top++;
        stack[top]="(";
        for(int i=0;i<index;i++){
            if(!isOperator(strFormula[i])&&!isOpeningParentheses(strFormula[i])&&!isClosingParentheses(strFormula[i])){
                postfix[j++]=strFormula[i];
            }
            else{
                if(isOperator(strFormula[i])){
                    if(j-1>=0&&postfix[j-1]!=",")
                    postfix[j++]=",";
                    if(i-1<0&&strFormula[i]=="-"){
                     postfix[j++]=strFormula[i]+strFormula[i+1];
                     i++;
                     continue;
                    }
                    else if(strFormula[i]=="-"&&isOpeningParentheses(strFormula[i-1])){
                        postfix[j++]="0";
                        postfix[j++]=",";
                    }
                    if(top<0){
                        Toast.makeText(this, "Error : Incorrect expression", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    while(!isOpeningParentheses(stack[top])&&operatorPrecedence(stack[top])>=operatorPrecedence(strFormula[i])){
                        postfix[j++]=stack[top];
                        postfix[j++]=",";
                        if(top<0) {
                            Toast.makeText(this, "Error : Incorrect expression", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        top--;
                    }
                    top++;
                    stack[top]=strFormula[i];
                }
                else if(isClosingParentheses(strFormula[i])){
                    if(j-1>=0&&postfix[j-1]!=",")
                        postfix[j++]=",";
                    if(top<0) {
                        Toast.makeText(this, "Error : Incorrect expression", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    while(!isOpeningParentheses(stack[top])){
                        postfix[j++]=stack[top];
                        postfix[j++]=",";
                        if(top<0) {
                            Toast.makeText(this, "Error : Incorrect expression", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        top--;
                    }
                    if(top<0) {
                        Toast.makeText(this, "Error : Incorrect expression", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    top--;
                }
                else if(isOpeningParentheses(strFormula[i])){
                    top++;
                    stack[top]="(";
                }
            }
        }
        index--;

        Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
        intent.putExtra("size",j);
        intent.putExtra("postfix",postfix);
        startActivity(intent);
    }

    public boolean checkParentheses(){
        int top=-1;
        for(int i=0;i<index;i++){
            if(isOpeningParentheses(strFormula[i])) {
                top++;
                stack[top]="(";
            }else if(isClosingParentheses(strFormula[i])){
                if(top<0){
                    return false;
                }
                top--;
            }
        }
        if(top!=-1)
            return false;
        return true;
    }

    public boolean isBinaryOperator(String s){
        if(s.equals("-")||s.equals("+")||s.equals("/")||s.equals("*")||s.equals("%")||s.equals("^"))
            return true;
        else
            return false;
    }
    public boolean checkOperator(){
        for(int i=0;i<index-1;i++){
            if(isOperator(strFormula[i])&&isOperator(strFormula[i+1])){
                if(isBinaryOperator(strFormula[i])==isBinaryOperator(strFormula[i+1])){
                    return false;
                }
            }
            if(isOpeningParentheses(strFormula[i])&&isClosingParentheses(strFormula[i+1]))
                return false;
        }
        return true;
    }

    public void showButton(View view){
        if(index==0){
            Toast.makeText(this, "Error : Empty expression", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkParentheses()){
            Toast.makeText(this, "Error : Check parenthesis", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkOperator()){
            Toast.makeText(this, "Error : Check expression", Toast.LENGTH_SHORT).show();
            return;
        }
        infixToPostfix();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        formula=(TextView)findViewById(R.id.formula);
        table=(TableLayout)findViewById(R.id.table);
    }
}
