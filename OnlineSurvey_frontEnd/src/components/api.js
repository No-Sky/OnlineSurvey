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
    return res.data
}

export const sendVertify = data =>{
    const res = axios.get("/sendvertify", data)
    return res.data
}

export const register = data => {
    const res = axios.post('/register', data)
    return res.data
}

export const getQuestionnaireList = data => {
    const res = axios.get('/questionnaire/list', data)
    return res.data
}

export const addQuestionnaire = data => {
    const res = axios.post('/questionnaire', data)
    return res.data
}

export const pushQuestionnaire = data => {
    const res = axios.put('/questionnaire', data)
    return res.data
}

export const deleteQuestionnaire = data => {
    const res = axios.delete('/questionnaire', data)
    return res.data
}

export const getTempPage = data => {
    const res = axios.get('/temp', data)
    return res.data
}

