import React from 'react'
import "../components/startscan/Startscan.css";

import ScanService from '../service/ScanService'

// const createUser = (e) => {
//     createUser(this.state.user)
//         .then(response => {
//             console.log(response);
//             this.setState({numberOfUsers: this.state.numberOfUsers + 1})
//         });
//     this.setState({user: {}})
// }
//
// const getAllUsers = () => {
//     getAllUsers()
//         .then(users => {
//             console.log(users)
//             this.setState({users: users, numberOfUsers: users.length})
//         });
// }



const Startscan = () => {

    return (
        <div className="newScan">
      <h1 className="newUserTitle">New Scan</h1>
      <form className="newScanForm">
        <div className="newUserItem">
            <div className="newUserItem">
            <label>Choose Scan Type</label>
            <select className="newUserSelect" name="active" id="active">
                <option value="yes">Passive Scan</option>
                <option value="no">Active Scan</option>
            </select>
            </div>
            <div className="newUserItem">
            <label>Target URL</label>
                <input type="text" placeholder="https://example.com.tr" />
            </div>
        <button className="newUserButton">Start Scan</button>
        </div>
      </form>
    </div>
    )
}

export default Startscan
