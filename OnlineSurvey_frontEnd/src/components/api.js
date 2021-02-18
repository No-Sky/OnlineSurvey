/**
 * 程序名：api接口
 * 功能：与后端通讯的api接口定义
 */
import axios from "axios";

//问卷设计者操作
export const designOpera = async data => {
  const res = await axios.post("/api/design", data);
    return res.data;
};

//问卷回答者操作
export const answerOpera = async data => {
  const res = await axios.post("/api/answer", data);
    return res.data;
};

export const login = data => {
  const res = axios.post('/login', data)
            .then(function (res) {
                console.log(res);
                if (res.data.msg == '该用户不存在') {
                    that.logindata.erusername = res.data.msg;
                } else if (res.data.msg == '输入密码错误') {
                    that.logindata.erpass = res.data.msg;
                } else {
                    //登陆成功后设置了，localStorage 保存登陆状态和userid
                    localStorage.setItem("UserId", res.data.userId);
                    localStorage.setItem("Flag", "isLogin");
                    that.$store.dispatch("userLogin", true);
                    that.$router.push("/");
                }
            })
            .catch(function (error) {
                if (error) {
                    console.log('登录请求失败');
                }
            })
}
