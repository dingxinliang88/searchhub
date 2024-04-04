import axios from "axios";

const http = axios.create({
  baseURL: "http://localhost:8110/api",
  timeout: 1000,
  headers: {},
});

// Add a request interceptor
http.interceptors.request.use(
  function (config) {
    // Do something before request is sent
    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

// Add a response interceptor
http.interceptors.response.use(
  function (response) {
    const data = response.data;

    if (data.code === 0) {
      return data.data;
    }

    console.error("request error", data);
    return response.data;
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    return Promise.reject(error);
  }
);

export default http;
