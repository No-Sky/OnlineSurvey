/**
 * 程序名：api接口
 * 功能：与后端通讯的api接口定义
 */
import _axios from "axios";

let config = {
    // baseURL: process.env.baseURL || process.env.apiUrl || ""
    // timeout: 60 * 1000, // Timeout
    withCredentials: true, // Check cross-site Access-Control
  };
  
  const axios = _axios.create(config);

const base_url = "http://localhost:10001"


/** Base API */
export const login = data => {
    return axios.post(base_url + '/login', data)
}

export const sendVertify = data => {
    return axios.get(base_url + "/sendvertify", data)
}

export const register = data => {
    return axios.post(base_url + '/register', data)
}

export const resetPassword = data => {
    return axios.post(base_url +'/resetpass', data)
}

export const loginCheck= data => {
    return axios.post(base_url +'/logincheck', data)
}

/**  Questioinnaire API */
export const getQuestionnaireList = data => {
    return axios.get(base_url + '/questionnaire/user', data)
}

export const getQuestionnaireById = data => {
    return axios.get(base_url + '/questionnaire', data)
}

export const addQuestionnaire = data => {
    return axios.post(base_url + '/questionnaire', data)
}

export const pushQuestionnaire = data => {
    return axios.put(base_url + '/questionnaire', data)
}

export const deleteQuestionnaire = data => {
    return axios.delete(base_url + '/questionnaire', data)
}

export const getTempPage = data => {
    return axios.get(base_url + '/temp', data)
}

/** Question API */
export const getQuestionList = data => {
    return axios.get(base_url + '/question/list', data)
}

export const addQuestion = data => {
    return axios.post(base_url + '/question', data)
}

export const deleteQuestion = data => {
    return axios.delete(base_url + '/question', data)
}

/** Answer API */
export const getDataAnalysis = data => {
    return axios.get(base_url + '/answer/analysis', data)
}

export const getAnswerText = data => {
    return axios.get(base_url + '/answer/text', data)
}

export const addAnswer = data => {
    return axios.post(base_url + '/answer/text', data)
}