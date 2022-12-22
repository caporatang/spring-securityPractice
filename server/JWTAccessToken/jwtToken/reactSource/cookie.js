import {Cookies} from "react-cookie";

const cookies = new Cookies()

// export const setAuthTokenCookie = (name: string, value: string, option?: any) => {
//     return cookies.set(name, value, {...option});
// }
//
// export const getAuthTokenCookie = (name:string) => {
//     return cookies.get(name);
// }

export const setCookie = (name: string, value: string, options: any) => {
    return cookies.set(name, value, {...options});
}

export const getCookie = (name: string) => {
    return cookies.get(name);
}

// export const setCookie = (name: string, value: string, option?: any) => {
//     return cookies.set(name, value, {...option});
// }
//
// export const getCookie = (name:string) => {
//     return cookies.get(name);
// }

