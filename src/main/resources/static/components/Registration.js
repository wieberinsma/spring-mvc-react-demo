import React from "react";

export default function Registration(props)
{
    return (
        <div className="Auth-form-container">
            <form id="registration-form" className="Auth-form" acceptCharset="utf-8">
                <div className="Auth-form-content">
                    <h3 className="Auth-form-title">Sign Up</h3>
                    <div className="text-center">
                        Already registered?{" "}
                        <span className="link-primary" role="button" onClick={props.toggleLoginComponent}>
                            Sign In
                        </span>
                    </div>
                    <div className="form-group mt-3">
                        <label htmlFor="firstName">First name</label>
                        <input name="firstName" className="form-control mt-1" placeholder="First name"
                               onChange={(e) => props.setUserData('firstName', e.target.value)}
                        />
                    </div>
                    <div className="form-group mt-3">
                        <label htmlFor="lastname">Lastname</label>
                        <input name="lastname" className="form-control mt-1" placeholder="Lastname"
                               onChange={(e) => props.setUserData('lastname', e.target.value)}
                        />
                    </div>
                    <div className="form-group mt-3">
                        <label htmlFor="username">Username</label>
                        <input name="username" className="form-control mt-1" placeholder="Username"
                               onChange={(e) => props.setUserData('username', e.target.value)}/>
                    </div>
                    <div className="form-group mt-3">
                        <label htmlFor="password">Password</label>
                        <input name="password" type="password" className="form-control mt-1" placeholder="Password"
                               onChange={(e) => props.setUserData('password', e.target.value)}
                        />
                    </div>
                    <div className="d-grid gap-2 mt-3">
                        <button id="submit" type="button" className="btn btn-primary" onClick={props.sendRegistrationRequest}>
                            Register
                        </button>
                    </div>
                </div>
            </form>
        </div>
    );
}
