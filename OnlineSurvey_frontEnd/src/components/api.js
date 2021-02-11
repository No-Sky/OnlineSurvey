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
