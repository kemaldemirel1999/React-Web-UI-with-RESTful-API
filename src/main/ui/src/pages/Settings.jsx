import React from 'react'

const Settings = () => {
    return (
            <div className="newUser">
        <h1 className="newUserTitle">Settings</h1>
        <form className="newUserForm">
            <div className="newUserItem">
                <label>OwaspZAP Port Number</label>
                <input type="text" placeholder="8080" />
            </div>
            <div className="newUserItem">
                <label>OwaspZAP API Key</label>
                <input type="text" placeholder="xxxxxxxxxxxxx" />
            </div>
            <div className="newUserItem">
                <label>OwaspZAP Address</label>
                <input type="email" placeholder="localhost" />
            </div>
            <button className="newUserButton">Apply</button>
        </form>
        </div>
    )
}

export default Settings
