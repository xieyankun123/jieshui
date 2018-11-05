package com.xyk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xyk.model.*;
import com.xyk.service.*;
import com.xyk.util.DateUtil;
import com.xyk.util.HttpOutUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@RequestMapping("/apparatus")
public class yqController {
    @Resource
    private yqService ys;
    @Resource
    private userService us;
    @Resource
    private roomService rs;
    @Resource
    private gyService gs;
    @Resource
    private u_rService ur;
    @Resource
    private apService as;
    @Resource
    private apdataService ads;
    @RequestMapping("/tp")
    public ModelAndView tp(HttpServletRequest request)
    {
        ModelAndView mv=new ModelAndView();
        String apparatus_id=request.getParameter("apparatus_id");
        mv.addObject("apparatus_id",apparatus_id);
        mv.setViewName("zhexian");
        System.out.println("test1"+apparatus_id);
        return mv;
    }
    @RequestMapping("/")
    public void list(HttpServletResponse response)
    {
        JSONObject result=new JSONObject();
        result.put("list",ys.list());
        HttpOutUtil.outData(response,JSONObject.toJSONString(result));
    }
    @RequestMapping("/update")
    public void update(HttpServletResponse response,yqModel yq)
    {
        JSONObject result=new JSONObject();
        ys.update(yq);
        result.put("msg","更新成功");
        HttpOutUtil.outData(response,JSONObject.toJSONString(result));
    }
    @RequestMapping("/yqinfo")
    public void yqinfo(HttpServletRequest request, HttpServletResponse response)
    {
        JSONObject result=new JSONObject();
        String user_telephone=request.getParameter("user_telephone");
        if(user_telephone.equals("false"))
        {
            result.put("msg","fail");
        }
        else {
            //UserModel user=us.selbytel(user_telephone);
            String room_id = ur.selbyUtel(user_telephone).get(ur.selbyUtel(user_telephone).size() - 1).getRoom_id();
            //roomModel room=rs.selbyRid(room_id);
            //room.setOwn(gs.selbyid(room.getApartment_id()).getOwner());
            // result.put("msg1",user);
//        if(room_id.endsWith("0")) {
//            List<yqModel> apparatus0 = ys.selbyRid(room_id);
//            mv.addObject("room")
//          //  result.put("msg2",apparatus0);
//          //  result.put("resule","10013");
//        }
//        else
//        {
            List<yqModel> apparatus1 = ys.selbyRid(room_id);
            System.out.println(apparatus1.get(0).getBeizhu2());
            System.out.println(apparatus1.get(1).getBeizhu2());
            StringBuilder a = new StringBuilder(room_id);
            a.setCharAt(4, '0');
            List<yqModel> apparatus0 = ys.selbyRid(a.toString());
            result.put("apparatus1", apparatus1);
            result.put("apparatus0", apparatus0);
        }
         HttpOutUtil.outData(response,JSONObject.toJSONString(result));
    }
    @RequestMapping("/yqinfoU")
    public ModelAndView yqinfoU(HttpServletRequest request, HttpServletResponse response)
    {
       // JSONObject result=new JSONObject();
        ModelAndView mv=new ModelAndView();
        String room_id=request.getParameter("room_id");
        String user_telephone=request.getParameter("user_telephone");
        System.out.println(room_id);
        System.out.println(user_telephone);
        UserModel user=us.selbytel(user_telephone);
        roomModel room=rs.selbyRid(room_id);
        room.setOwn(gs.selbyid(room.getApartment_id()).getOwner());
        mv.addObject("user",user);
        mv.addObject("room",room);
       // result.put("msg1",user);
//        if(room_id.endsWith("0")) {
//            List<yqModel> apparatus0 = ys.selbyRid(room_id);
//            mv.addObject("room")
//          //  result.put("msg2",apparatus0);
//          //  result.put("resule","10013");
//        }
//        else
//        {
            List<yqModel> apparatus1 = ys.selbyRid(room_id);
            StringBuilder a=new StringBuilder(room_id);
            a.setCharAt(4, '0');
            List<yqModel> apparatus0 = ys.selbyRid(a.toString());
            mv.addObject("apparatus1",apparatus1);
            mv.addObject("apparatus0",apparatus0);
            mv.setViewName("user_power_consumption");
            System.out.println(a.toString());
            return mv;
           // result.put("msg2",apparatus1);
           // result.put("msg3",apparatus0);
       // }
       // HttpOutUtil.outData(response,JSONObject.toJSONString(result));
    }
    @RequestMapping("/yqinfoR")
    public ModelAndView yqinfoR(HttpServletRequest request, HttpServletResponse response)
    {
        // JSONObject result=new JSONObject();
        ModelAndView mv=new ModelAndView();
        String room_id=request.getParameter("room_id");
        System.out.println(room_id);
        roomModel room=rs.selbyRid(room_id);
        room.setOwn(gs.selbyid(room.getApartment_id()).getOwner());
        mv.addObject("room",room);
        // result.put("msg1",user);
//        if(room_id.endsWith("0")) {
//            List<yqModel> apparatus0 = ys.selbyRid(room_id);
//            mv.addObject("room")
//          //  result.put("msg2",apparatus0);
//          //  result.put("resule","10013");
//        }
//        else
//        {
        List<yqModel> apparatus1 = ys.selbyRid(room_id);
        StringBuilder a=new StringBuilder(room_id);
        a.setCharAt(4, '0');
        List<yqModel> apparatus0 = ys.selbyRid(a.toString());
        mv.addObject("apparatus1",apparatus1);
        mv.addObject("apparatus0",apparatus0);
        mv.setViewName("applicance_state");
        System.out.println(a.toString());
        return mv;
        // result.put("msg2",apparatus1);
        // result.put("msg3",apparatus0);
        // }
        // HttpOutUtil.outData(response,JSONObject.toJSONString(result));
    }

    @RequestMapping("/onoff")
    public void httpURLConectionGET1(int actionID,HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        String user_telephone=request.getParameter("user_telephone");
        System.out.println(id);
        System.out.println(actionID);
        JSONObject result=new JSONObject();
        String a="0";
        try {
            //String username=us.selbytel(user_telephone).getUser_name();
            yqModel yq=ys.selbyid(id);
            String apparatus_id=yq.getApparatus_id();
            String useable=yq.getUseable();
            if(useable.equals(a)&&actionID==2)
            {
                try {
                   // System.out.println(apparatus_id);
                    //System.out.println(useable);
                    URL url = new URL("http://ss1.chakonger.net.cn/web/action?actionID=2&inst=0&token="+apparatus_id+"");    // 把字符串转换为URL请求地址
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
                    connection.connect();// 连接会话
                    // 获取输入流
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {// 循环读取流
                        sb.append(line);
                    }
                    br.close();// 关闭流
                    connection.disconnect();// 断开连接
                    System.out.println(sb.toString());
                    String msg0=sb.toString();
                    JSONObject json= JSON.parseObject(msg0);
                    String b=json.getString("devStatus");
                    if(b.equals("A003")||b.equals("A004")||b.equals("A005"))
                    {
                        ys.upbyid(user_telephone,id);
                        ApModel am=new ApModel();
                        am.setApparatus_id(id);
                        DateUtil dateUtil=new DateUtil();
                        am.setTime(dateUtil.getTime());
                        am.setUser_name_on(user_telephone);
                        as.add(am);
                        result.put("msg","10001");
                    }
                    else if(b.equals("A002"))
                    {
                        result.put("msg","插控儿没有连接上wifi");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("失败!");
                }

            }
            else
            {
                if(useable.equals(user_telephone)&&actionID==0)
                {
                    try {
                        URL url = new URL("http://ss1.chakonger.net.cn/web/action?actionID=0&inst=0&token="+apparatus_id+"");    // 把字符串转换为URL请求地址
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
                        connection.connect();// 连接会话
                        // 获取输入流
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                        String line;
                        StringBuilder sb = new StringBuilder();
                        while ((line = br.readLine()) != null) {// 循环读取流
                            sb.append(line);
                        }
                        br.close();// 关闭流
                        connection.disconnect();// 断开连接
                        System.out.println(sb.toString());
                        JSONObject json= JSON.parseObject(sb.toString());
                        String b=json.getString("devStatus");
                        if(b.equals("A003")||b.equals("A004")||b.equals("A005"))
                        {
                            ys.upbyid(a,id);
                            ApModel am=new ApModel();
                            am.setApparatus_id(id);
                            DateUtil dateUtil=new DateUtil();
                            am.setTime(dateUtil.getTime());
                            am.setUser_name_off(user_telephone);
                            as.add(am);
                            result.put("msg","10002");
                        }
                        else if(b.equals("A002"))
                        {
                            result.put("msg","插控儿没有连接上wifi");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("失败!");
                    }
                    }
                else
                {
                    result.put("msg","10003");
                }
            }
        }
        catch(Exception e)
        {
            result.put("msg",e);
        }
        HttpOutUtil.outData(response,JSONObject.toJSONString(result));
    }
    public static HttpURLConnection connection(String URL
    ) throws Exception {
        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");
        connection.connect();
        return connection;
        // TODO Auto-generated method stub
    }
    @RequestMapping("PowerAndValue")
    public void PoweAndValue(HttpServletResponse response,HttpServletRequest request)
    {
        JSONObject result=new JSONObject();
        String user_telephone=request.getParameter("user_telephone");;
        if(user_telephone.equals("false"))
        {
            result.put("msg","fail");
        }
        else {
            String room_id = ur.selbyUtel(user_telephone).get(ur.selbyUtel(user_telephone).size() - 1).getRoom_id();
            List<yqModel> apparatus1 = ys.selbyRid(room_id);
            StringBuilder a = new StringBuilder(room_id);
            a.setCharAt(4, '0');
            List<yqModel> apparatus0 = ys.selbyRid(a.toString());
            List<EleModel> eleModels1=new ArrayList<EleModel>();
            List<EleModel> eleModels0=new ArrayList<EleModel>();
            DecimalFormat df = new DecimalFormat("0.00");
            for(int i=0;i<apparatus1.size();i++)
            {
                EleModel e=new EleModel();
                e.setYq(apparatus1.get(i));
                List<apdataModel> apdata1 = ads.selbynameP(user_telephone,apparatus1.get(i).getId());
                double sum = 0;
                for (int j = 0; j < apdata1.size(); j++) {
                    double value = Double.parseDouble(apdata1.get(j).getValue());
                    sum = sum + value / (30 * 1000);
                }
                String ssum=df.format(sum);
                e.setSum(ssum);
                List<apdataModel> apdata2=ads.selbyid(apparatus1.get(i).getId());
                e.setApdata(apdata2);
                eleModels1.add(e);
            }
            for(int i=0;i<apparatus0.size();i++)
            {
                EleModel e=new EleModel();
                e.setYq(apparatus0.get(i));
                List<apdataModel> apdata1 = ads.selbynameP(user_telephone,apparatus0.get(i).getId());
                double sum = 0;
                for (int j = 0; j < apdata1.size(); j++) {
                    double value = Double.parseDouble(apdata1.get(j).getValue());
                    sum = sum + value / (30 * 1000);
                }
                String ssum=df.format(sum);
                e.setSum(ssum);
                List<apdataModel> apdata2=ads.selbyid(apparatus0.get(i).getId());
                e.setApdata(apdata2);
                eleModels0.add(e);
            }
            result.put("siyou",eleModels1);
            result.put("gongyou",eleModels0);
        }
        HttpOutUtil.outData(response,JSONObject.toJSONString(result));
    }
    @RequestMapping("PowerValue")
    public void PoweValue(HttpServletResponse response,HttpServletRequest request)
    {
        JSONObject result=new JSONObject();
        String apparatus_id=request.getParameter("apparatus_id");
        String name=request.getParameter("user_telephone");
        List<apdataModel> apdata = ads.selbynameP(name,apparatus_id);
        double sum=0;
        for(int j=0;j<apdata.size();j++)
        {
            double value= Double.parseDouble(apdata.get(j).getValue());
            sum=sum+value/(30*1000);
        }
        result.put("apdata",apdata);
        result.put("sum",sum);
        HttpOutUtil.outData(response,JSONObject.toJSONString(result));
    }
    @RequestMapping("updateBeizhu")

    public void updateBeizhu(HttpServletRequest request,HttpServletResponse response)
    {
        JSONObject result=new JSONObject();
        String beizhu=request.getParameter("beizhu");
        String id=request.getParameter("id");
        try {
            boolean b=ys.updateBeizhu(beizhu,id);
            if(b)
            result.put("msg","更新成功");
            else {
            result.put("msg","更新失败");}
        }
        catch (Exception r)
        {result.put("msg","更新失败");}
        HttpOutUtil.outData(response,JSONObject.toJSONString(result));
    }
}
