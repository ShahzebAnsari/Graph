package com.example.graph;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    TextView expression;
    boolean error=false;
    String[] postfix=new String[200];
    int size;

    //Graph Part
    int image=0;
    LinearLayout seekBarLayout;
    LinearLayout verticalGrid;
    RelativeLayout textX;
    LinearLayout horizontalGrid;
    RelativeLayout textY;
    LinearLayout graphPoint;
    double inputScaling=1;
    double outputScaling=1;
    int progressionX=300;
    int progressionY=300;
    ImageView circle;
    int pointProgression=179;
    double pointInput=0;
    double pointOutput;
    TextView textPointMove;

    //Graph Part End


    public double cos(double x){
        return sin(x+3.14159265/2);
    }

    public double tan(double x){
        if(x<0)return -tan(-x);
        if(x==0.7853981625)
            return 1;
        return sin(x)/((double)sin(x+1.57079633));
    }


    public double exp(double x){
        double ans=1;
        for(int i=1;i<30;i++){
            double term=1;
            for(int j=1;j<=i;j++){
                term*=x/(float)j;
            }
            ans+=term;
        }
        return ans;
    }

    public double pow(double a,double x)
    {
        if(x<0)return 1/((double)pow(a,-x));
        if(a==0||x==0) {
            if (x == 0) return 1;
            if (a == 0) return 0;
        }
        int b=(int)x;
        double c=x-b;
        double ans=1;
        for(int i=1;i<=b;i++)
            ans=ans*a;
        if(c==0)return ans;
        double ans2=exp(c*log(a));
        return ans*ans2;
    }

    public double sin(double x){
        if(x==3.14159265) return 0;
        if(x<0) return -sin(-x);
        while(x>=6.28318531){
            x-=6.28318531;
        }
        double b=0;
        for(int i=1;i<200;i=i+2)
        {
            double c=1;
            for(int j=1;j<=i;j++)
            {
                c=c*x/(double)j;
            }
            if(i%4==1)
                b+=c;
            else
                b-=c;
        }
        return b;
    }

    public double log(double x){
        if(x<0)return 0;
        double b=0;
        double e=2.71828183;
        while(x>=1){
            x=x/e;
            b++;
        }
        x=1-x;
        for(int i=1;i<20;i++){
            double c=1;
            for(int j=1;j<=i;j++){
                c*=x;
            }
            c=c/i;
            b-=c;
        }
        return b;
    }

    public boolean isOperator(String s){
        if(s.equals("log")||s.equals("sin")||s.equals("cos")||s.equals("tan")||s.equals("%")||s.equals("^")||s.equals("/")||s.equals("*")||s.equals("-")||s.equals("+"))
            return true;
        else
            return false;
    }
    public boolean isBinaryOperator(String s){
        if(s.equals("-")||s.equals("+")||s.equals("/")||s.equals("*")||s.equals("%")||s.equals("^"))
            return true;
        else
        return false;
    }

    public double evaluationOfPostfix(String[] postfix,int size,double x)
    {
        double[] stack=new double[100];
        int top=-1;
        error=false;
        start:
        for(int i=0;i<size;i++){
            if(!isOperator(postfix[i])){
                if((i-1<0||postfix[i-1].equals(","))&&postfix[i].equals(".")&&postfix[i+1].equals(",")){
                    expression.setText("Error : '.' can't come alone");
                    error=true;
                    break start;
                }
                if(postfix[i].equals("-x")){
                    top++;
                    stack[top]=-x;
                    i++;
                }else if(postfix[i].equals("-pi")){
                    top++;
                    stack[top]=-3.14159265;
                    i++;
                }else if(postfix[i].equals("-ex")){
                    top++;
                    stack[top]=-2.71828183;
                    i++;
                }
                else if(postfix[i].equals("x")){
                    top++;
                    stack[top]=x;
                    i++;
                }else if(postfix[i].equals("pi")){
                    top++;
                    stack[top]=3.14159265;
                    i++;
                }else if(postfix[i].equals("ex")){
                    top++;
                    stack[top]=2.71828183;
                    i++;
                }
                else {
                    String tempstr = "";
                    while (!postfix[i].equals( ",")) {
                        tempstr += postfix[i];
                        i++;
                    }
                    double temp = Double.parseDouble(tempstr);
                    top++;
                    stack[top] = temp;
                }
            }else{
                if(isBinaryOperator(postfix[i])){
                    switch (postfix[i]){
                        case "+":if(top<=0){
                            expression.setText("Error : Check expression");
                            error=true;
                            break start;
                        }
                            stack[top-1]=stack[top-1]+stack[top]; top--; break;
                        case "-":if(top<=0){
                            expression.setText("Error : Check expression");
                            error=true;
                            break start;
                        }
                            stack[top-1]=stack[top-1]-stack[top]; top--; break;
                        case "*":if(top<=0){
                            expression.setText("Error : Check expression");
                            error=true;
                            break start;
                        }
                            stack[top-1]=stack[top-1]*stack[top]; top--; break;
                        case "/":if(top<=0){
                            expression.setText("Error : Check expression");
                            error=true;
                            break start;
                        }
                            try{stack[top-1]=stack[top-1]/stack[top];}catch (Exception e){e.printStackTrace();} top--; break;
                        case "%":if(top<=0){
                            expression.setText("Error : Check expression");
                            error=true;
                            break start;
                        }
                            if(stack[top-1]!=(int)stack[top-1]||stack[top]!=(int)stack[top]){
                                expression.setText("Error : Remainder of float value");
                                error=true;
                                break start;
                            }else{
                                stack[top-1]=((int)stack[top-1])%((int)stack[top]); top--;} break;
                        case "^":if(top<=0){
                            expression.setText("Error : Check expression");
                            error=true;
                            break start;
                        }if(stack[top-1]==0&&stack[top]==0)
                        {
                            expression.setText("NaN");
                            error=true;
                            break start;
                        }
                            stack[top-1]=pow(stack[top-1],stack[top]); top--; break;
                        default:break;
                    }

                }
                else{
                    switch (postfix[i]){
                        case "log":
                            if(top<0){
                                expression.setText("Error : Check expression");
                                error=true;
                                break start;
                            }
                            if(stack[top]<=0){
                                if(stack[top]==0)
                                    expression.setText("Error : Log has 0 input");
                                else
                                    expression.setText("Error : Log has negative input");
                                error=true;
                                break start;
                            }else{
                                stack[top]=log(stack[top]);break;}
                        case "sin":
                            if(top<0){
                                expression.setText("Error : Check expression");
                                error=true;
                                break start;
                            }
                            stack[top]=sin(stack[top]);break;
                        case "cos":
                            if(top<0){
                                expression.setText("Error : Check expression");
                                error=true;
                                break start;
                            }
                            stack[top]=cos(stack[top]);break;
                        case "tan":
                            if(top<0){
                                expression.setText("Error : Check expression");
                                error=true;
                                break start;
                            }
                            if(stack[top]==3.14159265/2||stack[top]==-3.14159265/2){
                                expression.setText("Infinity");
                                error=true;
                                break start;
                            }else{
                                stack[top]=tan(stack[top]);break;}
                        default:break;
                    }
                }
                i++;
            }
        }
        if(error==false) {
            if(top==-1){
                expression.setText("Error : Check expression");
                error=true;
                return -1.01;
            }
            double ans=stack[top];
            return ans;
        }
        return 0;
    }

    //Graph Part
    public void scrollOpen(View view){
        ((ImageView) view).animate().translationY((image-1)*220).setDuration(500);
        seekBarLayout.animate().translationY(340+(image-1)*220).setDuration(500);
        if(image==0) {
            ((ImageView) view).setBackgroundResource(R.drawable.button2);
            image=1;
        }
        else {
            ((ImageView) view).setBackgroundResource(R.drawable.button);
            image=0;
        }
    }


    public double pow1(double a,double x)
    {
        if(x<0){
            return 1/((double)pow1(a,-x));
        }
        if(a==0||x==0) {
            if (x == 0) return 1;
            if (a == 0) return 0;
        }
        int b=(int)x;
        double c=x-b;
        double ans=1;
        for(int i=1;i<=b;i++)
            ans=ans*a;
        if(c==0)return ans;
        double ans2=exp(c*log(a));
        return ans*ans2;
    }

    public void drawGraph(double inputScalingFactor,double outputScalingFactor){
        double prev=72*fun((-179)*10/(double)360*inputScalingFactor)/(double)outputScalingFactor;
        double next=72*fun((1-179)*10/(double)360*inputScalingFactor)/(double)outputScalingFactor;
        for(int i=0;i<360;i++){
            double x=72*fun((i-179)*10/(double)360*inputScalingFactor)/(double)outputScalingFactor;
            if((int)next>(int)x){
                (graphPoint.getChildAt(i)).setLayoutParams(new LinearLayout.LayoutParams(2,(int)(next+1-x)+1));
                if((next-x)%2==0)
                    (graphPoint.getChildAt(i)).animate().translationY((int)(-x-(next-x)/2)).setDuration(2000);
                else
                    (graphPoint.getChildAt(i)).animate().translationY((int)(-x-(next-x)/2)).setDuration(2000);
            }
            else
            if((int)prev>(int)x){
                (graphPoint.getChildAt(i)).setLayoutParams(new LinearLayout.LayoutParams(2,(int)(prev+1-x)+1));
                if((prev-x)%2==0)
                    (graphPoint.getChildAt(i)).animate().translationY((int)(-x-(prev-x)/2)).setDuration(2000);
                else
                    (graphPoint.getChildAt(i)).animate().translationY((int)(-x-(prev-x)/2)).setDuration(2000);
            }
            else{
                (graphPoint.getChildAt(i)).animate().translationY((int)(-x)).setDuration(2000);
            }
            prev=x;
            next=72*fun((i+2-179)*10/(double)360*inputScalingFactor)/(double)outputScalingFactor;


        }
    }
    public void drawGraph1(double inputScalingFactor,double outputScalingFactor){
        double prev=72*fun((-179)*10/(double)360*inputScalingFactor)/(double)outputScalingFactor;
        double next=72*fun((1-179)*10/(double)360*inputScalingFactor)/(double)outputScalingFactor;
        for(int i=0;i<360;i++){
            double x=72*fun((i-179)*10/(double)360*inputScalingFactor)/(double)outputScalingFactor;
            (graphPoint.getChildAt(i)).animate().translationY((int)(-x)).setDuration(2000);
            prev=x;
            next=72*fun((i+2-179)*10/(double)360*inputScalingFactor)/(double)outputScalingFactor;


        }
    }
    public double pointPosition(int input){
        double x=72*fun((input-179)*10/(double)360*inputScaling)/(double)outputScaling;
        return x;
    }

    public double pow(double a,int b){
        if(b==0)return 1;
        double ans=1;
        if(b>0) {
            for (int i = 0; i < b; i++) {
                ans = ans * a;
            }
        }else{
            b=-b;
            for(int i=0;i<b;i++)
                ans=ans/(double) a;
        }
        return ans;
    }

    public double fun(double x){
        return evaluationOfPostfix(postfix,size,x);
    }


    //Graph Part End

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        expression=(TextView)findViewById(R.id.expression);
        Intent intent=getIntent();
        size=intent.getIntExtra("size",0);
        postfix=intent.getStringArrayExtra("postfix");
        size--;
        /*double ans=evaluationOfPostfix(postfix,size);
        if(error==false) {
            int ans1 = (int) ans;
            if (ans1 == ans)
                expression.setText(String.valueOf(ans1));
            else
                expression.setText(String.valueOf(ans));
        }*/
        //Graph Part

        seekBarLayout=(LinearLayout)findViewById(R.id.seekBarLayout);
        verticalGrid=(LinearLayout)findViewById(R.id.verticalGridLinearLayout);
        textX=(RelativeLayout)findViewById(R.id.textX);
        horizontalGrid=(LinearLayout)findViewById(R.id.horizontalGridLinearLayout);
        textY=(RelativeLayout)findViewById(R.id.textY);
        graphPoint=(LinearLayout)findViewById(R.id.graphPointLinearLayout);
        circle=(ImageView)findViewById(R.id.circle);
        circle.setTranslationY(-(int)pointPosition(179));

        seekBarLayout.setTranslationY(340);


        //ScalingX seekBar
        for(int i=0;i<19;i++){
            (verticalGrid.getChildAt(i)).setTranslationX((i-9)*36);
            ((TextView)textX.getChildAt(i)).setTranslationX((i-9)*36);
        }

        final TextView textScalingX=(TextView)findViewById(R.id.textScalingX);

        final SeekBar scalingX=(SeekBar)findViewById(R.id.scalingX);
        scalingX.setMax(1000);
        scalingX.setProgress(300);
        scalingX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int originalProgress, boolean fromUser) {
                if(originalProgress==0){
                    originalProgress=1;
                    scalingX.setProgress(1);
                }
                textScalingX.setText("ScalingX : "+String.valueOf(originalProgress));
                int progress=originalProgress+200;
                if(progress>=200){progress=progress%200;
                    if(progress<100)
                        progress+=100;
                }
                progressionX=originalProgress;
                int gridNo = 19;
                for (int i = 0; i < gridNo; i++) {
                    int[] location = new int[2];
                    (verticalGrid.getChildAt(i)).getLocationOnScreen(location);
                    int currentPosition = 360 + 78 * (i - 9);
                    int newPosition = 360 + (int) ((currentPosition - 360) * 100 / ((double) progress));
                    ((TextView)textX.getChildAt(i)).setText(String.valueOf((i-9)*pow(2,(int)(originalProgress/100-3))));
                    (verticalGrid.getChildAt(i)).setTranslationX((i-9)*36+newPosition - currentPosition);
                    ((TextView)textX.getChildAt(i)).setTranslationX((i-9)*36+newPosition - currentPosition);


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                inputScaling=pow1(2,progressionX/(double)100-3);
                //drawGraph(inputScaling,outputScaling);
                drawGraph1(inputScaling,outputScaling);
                circle.setTranslationY(-(int)pointPosition(pointProgression));
                pointInput=(pointProgression-179)*10/(double)360*inputScaling;
                pointOutput=fun(pointInput);
                textPointMove.setText("Point x : "+String.format("%.4f", pointInput)+", y : "+String.format("%.4f", pointOutput));
            }
        });


        //ScalingY seekBar
        for(int i=0;i<31;i++){
            (horizontalGrid.getChildAt(i)).setTranslationY((i-15)*36);
            ((TextView)textY.getChildAt(i)).setTranslationY((i-15)*36);

        }

        final TextView textScalingY=(TextView)findViewById(R.id.textScalingY);

        final SeekBar scalingY=(SeekBar)findViewById(R.id.scalingY);
        scalingY.setMax(1000);
        scalingY.setProgress(300);
        scalingY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int originalProgress, boolean fromUser) {
                if(originalProgress==0){
                    originalProgress=1;
                    scalingY.setProgress(1);
                }
                progressionY=originalProgress;
                textScalingY.setText("ScalingY : "+String.valueOf(originalProgress));
                int progress=originalProgress+200;
                if(progress>=200){
                    progress=progress%200;
                    if(progress<100)
                        progress+=100;
                }
                int gridNo = 31;
                for(int i=0;i<gridNo;i++){
                    int currentPosition=558+78*(i-15);
                    int newPosition=558+(int)((currentPosition-558)*100/((double)progress));
                    ((TextView)textY.getChildAt(i)).setText(String.valueOf(-(i-15)*pow(2,(int)(originalProgress/100-3))));
                    (horizontalGrid.getChildAt(i)).setTranslationY((i-15)*36+newPosition-currentPosition);
                    ((TextView)textY.getChildAt(i)).setTranslationY((i-15)*36+newPosition-currentPosition);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                outputScaling=pow1(2,progressionY/(double)100-3);
                //drawGraph(inputScaling,outputScaling);
                drawGraph1(inputScaling,outputScaling);
                circle.setTranslationY(-(int)pointPosition(pointProgression));
                pointInput=(pointProgression-179)*10/(double)360*inputScaling;
                pointOutput=fun(pointInput);
                textPointMove.setText("Point x : "+String.format("%.4f", pointInput)+", y : "+String.format("%.4f", pointOutput));
            }
        });
        for(int i=0;i<360;i++) {
            View view=new View(Main2Activity.this);
            view.setBackgroundColor(getResources().getColor(R.color.red));
            view.setLayoutParams(new LinearLayout.LayoutParams(2,2));
            graphPoint.addView(view);
        }
        //drawGraph(1,1);
        drawGraph1(1,1);
        pointOutput=fun(0);
        textPointMove=(TextView)findViewById(R.id.textPointMove);
        textPointMove.setText("Point x : "+String.valueOf(pointInput)+", y : "+String.valueOf(pointOutput));
        SeekBar pointMove=(SeekBar)findViewById(R.id.pointMove);
        pointMove.setMax(360);
        pointMove.setProgress(179);
        pointMove.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circle.setTranslationX((progress-179)*2);
                circle.setTranslationY(-(int)pointPosition(progress));
                pointProgression=progress;
                pointInput=(progress-179)*10/(double)360*inputScaling;
                pointOutput=fun(pointInput);
                textPointMove.setText("Point x : "+String.format("%.4f", pointInput)+", y : "+String.format("%.4f", pointOutput));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //Graph Part End
    }
}
