import {Link} from "react-router-dom";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import {useEffect, useState} from "react";
// import {Cookies} from "react-cookie";

function NavigationBar() {
    const navigate = useNavigate();

    const logout = () => {
        axios.post('/logout')
            .then(function res(response){
            })
            .catch(function (err){
                alert('로그아웃 실패 잠시 후 다시 시도해주세요');
            })
    }
    const StorageDelete = () => {
        localStorage.removeItem("auth_token");
        localStorage.removeItem("memberId");
    }

    return (
        <nav className="navbar navbar-expand-lg navbar-dark navbar-custom fixed-top">

            <div className="container px-5">
                <a className="navbar-brand" href="/">Trading Day</a>

                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation"><span className="navbar-toggler-icon"></span></button>
                <div className="collapse navbar-collapse" id="navbarResponsive">
                    <ul className="navbar-nav ms-auto">
                        <li className="nav-item"><a className="nav-link" href="/item/itemBoard">Trade!</a></li>
                        <li className="nav-item"><a className="nav-link" href="#!">Community</a></li>
                        <li className="nav-item"><a className="nav-link" href="/qna/qnaBoard">Support </a></li>

                        { localStorage.getItem("memberId") == null ?
                            <li className="nav-item"><a className="nav-link" href="/member/signup">Sign Up</a></li>
                            : <li className="nav-item"><a className="nav-link" href="/member/mypage">my Page</a></li> }

                        { localStorage.getItem("memberId") == null ?
                            <li className="nav-item"><a className="nav-link" href="/member/signin">Log In</a></li>
                            : <li className="nav-item"><a className="nav-link" href="/logout"
                                                          onClick={() => { logout()
                                                                           StorageDelete()}}>logout</a></li> }
                    </ul>
                </div>
            </div>
        </nav>
    )
}

export default NavigationBar;