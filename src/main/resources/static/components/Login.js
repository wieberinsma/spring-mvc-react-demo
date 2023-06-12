// Core libs
import React from "react";
import "static/external/css/bootstrap.min.css";

export default function Login(props)
{
    return (
        <div className="Auth-form-container">
            <form id="login-form" className="Auth-form" acceptCharset="utf-8">
                <div className="Auth-form-content">
                    <h3 className="Auth-form-title">Sign In</h3>
                    <div className="text-center">
                        Not registered yet?{" "}
                        <span className="link-primary" role="button" onClick={props.toggleLoginComponent}>
                            Sign Up
                        </span>
                    </div>
                    <div className="form-group mt-3">
                        <label htmlFor="username">Username</label>
                        <input type="email" name="username" className="form-control mt-1" placeholder="Enter username"/>
                    </div>
                    <div className="form-group mt-3">
                        <label htmlFor="password">Password</label>
                        <input type="password" name="password" className="form-control mt-1"
                               placeholder="Enter password"/>
                    </div>
                    <div className="d-grid gap-2 mt-3">
                        <button id="submit" type="button" className="btn btn-primary" onClick={props.sendLoginRequest}>
                            Login
                        </button>
                    </div>
                </div>
            </form>
        </div>
    );
}
