// Core libs
import React, {useState} from 'react';
import ReactDOM from 'react-dom';

// Components
import Login from "./static/components/Login";
import Registration from "./static/components/Registration";
import Private from "./static/components/Private";

// Styling
import "./App.css";

//TODO: Component functions als imports / via state manager
function App()
{
    const [status, setStatus] = useState('login');
    const [user, setUser] = useState({});

    let activeComponent = 'No content to display.';

    const toggleLoginComponent = () =>
    {
        setStatus(status === 'login' ? 'registration' : 'login');
    }

    /**
     * Use of form data object due to auto-generated form-login process of Spring Security, instead of JSON.stringify().
     */
    const loginRequest = () =>
    {
        const payload = new URLSearchParams();
        const formData = new FormData(document.getElementById("login-form"));

        for (let pair of formData)
        {
            payload.append(pair[0], pair[1]);
        }

        fetch('/login', {
            method: 'post',
            body: payload
        })
            .then(response =>
            {
                if (response.ok)
                {
                    return Promise.all([response.json(), response.headers]);
                }
                else
                {
                    return Promise.reject("Invalid credentials.")
                }
            })
            .then(response =>
                {
                    let json = response[0];

                    setUser({...user, username: json.username});
                    if (json.action === 'LOGIN')
                    {
                        loginRedirect(json.redirectUrl);
                    }
                }
            )
            .catch(message =>
            {
                alert(message);
            });
    }

    function loginRedirect(url)
    {
        fetch('/' + url, {
            method: 'get'
        })
            .then(response =>
            {
                if (response.status === 200)
                {
                    setStatus(url);
                    return Promise.resolve();
                }
                else
                {
                    return Promise.reject("Insufficient rights.")
                }
            });
    }

    const setUserData = (userProperty, userValue) => {
        setUser({...user, [userProperty]: userValue});
    }

    const registrationRequest = () =>
    {
        fetch("/register", {
            method: 'post',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'firstName': user.firstName,
                'lastname': user.lastname,
                'username': user.username,
                'password': user.password
            })
        })
            .then(response =>
            {
                if (response.ok)
                {
                    setStatus('login');
                    return Promise.resolve();
                }
                else
                {
                    return Promise.reject("Invalid registration provided.")
                }
            })
            .catch(message =>
            {
                alert(message);
            });
    }

    if (!status || status === 'login')
    {
        activeComponent = <Login sendLoginRequest={loginRequest} toggleLoginComponent={toggleLoginComponent}/>;
    }
    else if (status === 'registration')
    {
        return <Registration setUserData={setUserData} sendRegistrationRequest={registrationRequest} toggleLoginComponent={toggleLoginComponent}/>;
    }
    else if (status === 'private')
    {
        return <Private username={user.username}/>;
    }

    return (
        <div>
            {activeComponent}
        </div>
    );
}

ReactDOM.render(<App/>, document.getElementById("root"));
