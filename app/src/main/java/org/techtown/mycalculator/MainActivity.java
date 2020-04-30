package org.techtown.mycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Deque;

interface DIStack<E>{
    public boolean push(E item);
    public E pop();
    public E peek();
}

class DCStack<E> implements DIStack<E>{
    private Deque<E> deq;
    public DCStack(Deque<E>d) { deq=d; }
    public boolean push(E item) { return deq.offerFirst(item); }
    public E pop() { return deq.pollFirst(); }
    public E peek() { return deq.peekFirst(); }
}

public class MainActivity extends AppCompatActivity {
    String number="";
    String num1,num2,result,reSign,sign;//오른쪽 피연산자, 왼쪽 피연산자, 결과, 연산자, 부호 순.
    TextView textView;
    String offset="int";//정수냐 실수
    static String [] operator={"+","-","*","/"};
    static int [] priority={1,1,2,2};
    DIStack<String>  numberStack,operatorStack;
    int numberStackSize=0,operatorStackSize=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);
        numberStack=new DCStack<>(new ArrayDeque<String>());
        operatorStack=new DCStack<>(new ArrayDeque<String>());

        Button button1=findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                makeEmptyStack(numberStack);
                makeEmptyStack(operatorStack);
                numberStackSize=0;
                operatorStackSize=0;
            }
        });

        Button button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(sign==null) sign="-";
                else if(sign.equals("-")) sign="";
                else if(sign.equals("")) sign="-";
                number=sign;
                textView.setText(number);
            }
        });

        Button button3=findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int num1=Integer.parseInt(number);
                double num2=num1*0.01;
                offset="double";
                number=String.valueOf(num2);
                textView.setText(number);
            }
        });

        Button button4=findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                numberStack.push(number);
                numberStackSize++;
                textView.setText("");
                isPromising("/");
                number=null;
            }
        });

        Button button8=findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                numberStack.push(number);
                numberStackSize++;
                textView.setText("");
                isPromising("*");
                number=null;
            }
        });

        Button button12=findViewById(R.id.button12);
        button12.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                numberStack.push(number);
                numberStackSize++;
                textView.setText("");
                isPromising("-");
                number=null;
            }
        });

        Button button16=findViewById(R.id.button16);
        button16.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                numberStack.push(number);
                numberStackSize++;
                textView.setText("");
                isPromising("+");
                number=null;
            }
        });
        Button button5=findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { setNumber("7");}
        });
        Button button6=findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { setNumber("8");}
        });
        Button button7=findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { setNumber("9");}
        });
        Button button9=findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { setNumber("4");}
        });
        Button button10=findViewById(R.id.button10);
        button10.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { setNumber("5");}
        });
        Button button11=findViewById(R.id.button11);
        button11.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){ setNumber("6");}
        });
        Button button13=findViewById(R.id.button13);
        button13.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {setNumber("1");}
        });
        Button button14=findViewById(R.id.button14);
        button14.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { setNumber("2");}
        });
        Button button15=findViewById(R.id.button15);
        button15.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {setNumber("3");}
        });
        Button button17=findViewById(R.id.button17);
        button17.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { setNumber("0");}
        });
        Button button18=findViewById(R.id.button18);
        button18.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                number=number.concat(".");
                offset="double";
            }
        });
        Button button19=findViewById(R.id.button19);
        button19.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                numberStack.push(number);
                numberStackSize++;
                while(numberStackSize!=1&&operatorStackSize!=0){
                    num2=numberStack.pop(); numberStackSize--;
                    reSign=operatorStack.pop(); operatorStackSize--;
                    num1=numberStack.pop(); numberStackSize--;
                    if(offset.equals("int")) result=calculating(Integer.parseInt(num1),Integer.parseInt(num2),reSign);
                    else result=calculating(Double.parseDouble(num1),Double.parseDouble(num2),reSign);
                    numberStack.push(result); numberStackSize++;
                }
                textView.setText(numberStack.pop()); numberStackSize--;
            }
        });
    }
    private void isPromising(String data){
        String data2;
        if((data.equals("+")&&operatorStack.peek()!=null)||(data.equals("-")&&operatorStack.peek()!=null)) {
            data2=operatorStack.peek();
            if(data2.equals("*")||data2.equals("/"))  semiCalculate(data2);
            else if(data2.equals("+")||data2.equals("-")) semiCalculate(data2);
        }
        else if((data.equals("*")&&operatorStack.peek()!=null)||(data.equals("/")&&operatorStack.peek()!=null)){
            data2=operatorStack.peek();
            if(data2.equals("*")||data2.equals("/")) semiCalculate(data2);
        }
        operatorStack.push(data); operatorStackSize++;
    }
    private void semiCalculate(String data){
        num2=numberStack.pop(); numberStackSize--;
        num1=numberStack.pop(); numberStackSize--;
        if(offset.equals("int")) result=calculating(Integer.parseInt(num1),Integer.parseInt(num2),data);
        else result=calculating(Double.parseDouble(num1),Double.parseDouble(num2),data);
        numberStack.push(result); numberStackSize++;
        operatorStack.pop(); operatorStackSize--;
    }
    private <E> void makeEmptyStack(DIStack<E> stack){
        while(stack.peek()!=null) stack.pop();
        number="";
        textView.setText(number);
        sign="";
        offset="int";
    }
    private void setNumber(String num){
        if(number!=null)  number=number.concat(num);
        else number=num;
        textView.setText(number);
    }
    private <E extends Number> String calculating(E n1,E n2,String reSign) {
        if (offset.equals("int")) {
             if (reSign.equals("+"))  return String.valueOf(n1.intValue() + n2.intValue());
             else if (reSign.equals("-")) return String.valueOf(n1.intValue() - n2.intValue());
             else if (reSign.equals("*"))  return String.valueOf(n1.intValue() * n2.intValue());
             else  {
                 if(n1.intValue()%n2.intValue()!=0) {
                     offset="double";
                     return String.valueOf(n1.doubleValue()/n2.doubleValue());
                 }
                 else return String.valueOf(n1.intValue()/n2.intValue());
             }
        }
        else{
            if (reSign.equals("+"))  return String.valueOf(n1.doubleValue() + n2.doubleValue());
            else if (reSign.equals("-")) return String.valueOf(n1.doubleValue() - n2.doubleValue());
            else if (reSign.equals("*"))  return String.valueOf(n1.doubleValue() * n2.doubleValue());
            else  return String.valueOf(n1.doubleValue() / n2.doubleValue());
        }
    }
}
