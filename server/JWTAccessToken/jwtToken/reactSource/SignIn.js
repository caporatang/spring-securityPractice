
import style from "../../css/signInStyle.css";
import {Link, useNavigate} from "react-router-dom";
import {useRef} from "react";
import axios from "axios";
import { userNavigate } from "react-router-dom";
import Google from "../../components/Google";
import {GoogleLogin} from "@react-oauth/google";
import GoogleLoginButton from "../../components/GoogleLoginButton";
import {Container} from "react-bootstrap";
import styled from "styled-components";
import googleLoginImage from "../../img/googleSignInLight.png";
import GoogleButton from "react-google-button";

function SignIn() {

    const idRef = useRef(null);
    const pwdRef = useRef(null);
    const navigate = useNavigate();

    const loginOnClick = (e) =>  {
        e.preventDefault();

        const idVal = idRef.current.value;
        const pwdVal = pwdRef.current.value;

        if (idVal == "") {
            alert("아이디를 입력하세요");
            idRef.current.focus();
                return;
        } else if (pwdVal == "") {
            alert("비밀번호를 입력하세요");
            pwdRef.current.focus();
                return;
        }

        axios.post('/member/v1/login', {
            memberId : idVal,
            pwd : pwdVal
        },
            {withCredentials:true}
        ).then(function (res) {
            console.log(res);
            //"ROLE_USER"
            localStorage.setItem("userRole", res.data.authorities[0].authority)
            // auth_token은 로컬스토리지
            localStorage.setItem('auth_token', res.headers.auth_token);
            localStorage.setItem('memberId', res.data.username);
            window.location.assign("/");

        }).catch(function (err){
            alert('아이디와 비밀번호를 확인하세요');
        })
    } // login onClick


    // let test = '';
    // const onClick = () => {
    //     axios({
    //         url: '/login/oauth2/code/google',
    //         method: 'get',
    //         data: {
    //             AUTHORIZATION: test
    //         }
    //     }).then(function (res) {
    //         console.log(res)
    //     });
    // }

    // function LoginPage() {
    //     const login = useGoogleLogin({
    //         onSuccess: tokenResponse => {
    //             console.log(tokenResponse);
    //         },
    //     });

    function googleLoginLocation() {
        window.location.href="http://localhost:8080/oauth2/authorization/google";
        // axios.get('http://localhost:8080/oauth2/authorization/google'
        // ).then(function (res) {
        //     console.log(res);
        //     // window.location.assign("/");
        //
        // }).catch(function (err){
        //     console.log(err)
        //     alert('소셜 로그인 실패');
        // })
    }






    return(

        <div>
            <div id="con">
                <div id="login">
                    <div id="login_form">
                        <form onSubmit={loginOnClick}>
                            <h3 className="login" style={
                                {letterSpacing: -1, paddingBottom: 10}}>Happy Trade ~</h3>
                            <GoogleButton onClick={googleLoginLocation} style={{width:300}}/>

                            {/*<GoogleLoginButton />*/}
                            {/*<button type="button"*/}
                            {/*        onClick={googleLoginLocation}*/}
                            {/*        className="my-signin2"*/}
                            {/*        >*/}
                            {/*    <img src={googleLoginImage} alt="google" style={{width:300, height: 60}}/>*/}
                            {/*</button>*/}
                            {/*<button>*/}
                            {/*    <a href="/login/oauth2/code/google" className="btn btn-sm btn-success active"*/}
                            {/*       role="button">Google Login</a>*/}
                            {/*    <br/>*/}
                            {/*</button>*/}
                            {/*<Google />*/}

                            <hr />
                                <label>
                                    <p style={{textAlign: "left", fontSize: 12, color: "#666"}}>id</p>
                                    <input type="text" placeholder="아이디를 입력" className="size" ref={idRef}/>
                                    <p></p>
                                </label>
                            <br />
                                <label>
                                    <p style={{textAlign: "left", fontSize: 12, color: "#666"}}>Password </p>
                                    <input
                                        type="password" placeholder="비밀번호를 입력" className="size" ref={pwdRef}/>
                                </label>
                            <hr/>
                                <p>
                                    <input type="submit" value="Sign in" className="btn" style={{backgroundColor: "#217Af0"}}/>
                                </p>
                        </form>

                            <p className="find">
                                <span><a href="#" onClick={() => alert("기능 추가 예정 입니다")}>아이디 찾기</a></span>
                                <span><a href="#" onClick={() => alert("기능 추가 예정 입니다")}>비밀번호 찾기</a></span>
                                <span>
                                    <a href="/member/signup">회원가입 </a>
                                </span>
                            </p>
                    </div>
                </div>
            </div>
        </div>
    )
}


export default SignIn;