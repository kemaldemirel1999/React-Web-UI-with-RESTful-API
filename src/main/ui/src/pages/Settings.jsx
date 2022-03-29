import React from 'react'
import {Link, useHistory} from "react-router-dom";

const Settings = () => {

    const initialFormData = Object.freeze({
        portNumber: "",
        apiKey: "",
        zapAdress: ""
    });
    const history = useHistory();

    const [formData, updateFormData] = React.useState(initialFormData);

    const handleChange = (e) => {
        updateFormData({
            ...formData,

            // Trimming any whitespace
            [e.target.name]: e.target.value.trim()
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault()
        console.log(formData);
        fetch("http://localhost:8080/api/change-settings",{
            method: "POST",
            headers:{"Content-Type":"application/json" },
            body: JSON.stringify(formData.portNumber + " " + formData.apiKey + " " + formData.zapAdress)
        }).then( () =>{
                console.log("New settings applied")
            history.push("/")
            }
        )
    };

    return (
            <div className="newUser">
        <h1 className="newUserTitle">Settings</h1>
        <form className="newUserForm">
            <div className="newUserItem">
                <label>OwaspZAP Port Number</label>
                <input name="portNumber" placeholder="example:8080" onChange={handleChange}/>
            </div>
            <div className="newUserItem">
                <label>OwaspZAP API Key</label>
                <input name="apiKey" type="text" placeholder="example:1234567890" onChange={handleChange}/>
            </div>
            <div className="newUserItem">
                <label>OwaspZAP Address</label>
                <input name="zapAdress" type="text" placeholder="example:localhost" onChange={handleChange}/>
            </div>
            <button onClick={handleSubmit} className="newScanButton">Apply</button>
        </form>
        </div>
    )
}

export default Settings
