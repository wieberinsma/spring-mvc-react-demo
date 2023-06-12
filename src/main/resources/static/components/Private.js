import React from "react";

export default function Private(props) {
    return (
        <div className="Private-container">
            <h3 className="Private-title">Welcome {props.username}, you have reached a private resource!</h3>
        </div>
    )
}
