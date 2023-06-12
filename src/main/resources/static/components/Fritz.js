import React from "react";

function App() {
    const [status, setStatus] = React.useState("")
    let activeComponent;

    const [user, setUser] = React.useState({})

    const register = (user) => {
        // hier zou je dan eigenlijk een useEffect gebruiken die een fetch doet.
        setUser(user);
        setStatus("signedin");
    }

    // quasi routing
    if (status !== "signedin") {
        activeComponent = <Registration onRegister={register} />  // new Registration().component
    } else {
        activeComponent = <h1>Welcome {user.firstname}</h1>
    }

    return (
        <div>
            {activeComponent}

        </div>
    )
}

function Registration (props) {
    const [user, setUser] = React.useState({
        name: 'fritzvd',
        firstname: 'Fritz',
        password: ''
    });
    const onSubmit = (e) => {
        e.preventDefault();
        props.onRegister(user)
    }
    return (
        <form onSubmit={onSubmit}>
            <Input value={user.name} label={"Username"} onChange={(val) => setUser({...user, name: val})} />
            <Input value={user.firstname} label={"First Name"} onChange={(val) => setUser({...user, firstname: val})} />
            <Input type={"password"} value={user.password} label={"Password"} onChange={(val) => setUser({...user, password: val})} />
            <input type="submit" value="Register" />
        </form>
    )
}

function Input(props) {
    const type = props.type ? props.type : 'text';
    return (
        <div className="form-group">
            <label>{props.label}</label>
            <input type={type} value={props.value} onChange={(e) => props.onChange(e.target.value)} />
        </div>
    )
}
