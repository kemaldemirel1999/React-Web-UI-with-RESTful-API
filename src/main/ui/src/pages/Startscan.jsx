import React from 'react'
import "../components/startscan/Startscan.css";


const Startscan = () => {


    const initialFormData = Object.freeze({
        isItActiveScan: "",
        targetUrl: ""
    });

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
        if(formData.isItActiveScan.endsWith("yes")){
            fetch("http://localhost:8080/api/create-scan/active",{
                method: "POST",
                headers:{"Content-Type":"application/json" },
                body: JSON.stringify(formData.targetUrl)
            }).then( () =>{
                    console.log("New Scan Added")
                }
            )
        }
        else {
            fetch("http://localhost:8080/api/create-scan/passive",{
                method: "POST",
                headers:{"Content-Type":"application/json" },
                body: JSON.stringify(formData.targetUrl)
            }).then( () =>{
                    console.log("New Scan Added")
                }
            )
        }
    };


    return (
        <div className="newScan">
      <h1 className="newUserTitle">New Scan</h1>
      <form className="newScanForm" >
        <div className="newUserItem">
            <div className="newUserItem">
            <label>Choose Scan Type</label>
            <select className="newUserSelect" onChange={handleChange} name="isItActiveScan" >
                <option name="isItActiveScan" onChange={handleChange} value="yes">Active Scan</option>
                <option name="isItActiveScan" onChange={handleChange} value="no">Passive Scan</option>
            </select>
            </div>
            <div className="newUserItem">
            <label>Target URL</label>
                <input name="targetUrl" placeholder="https://example.com.tr" onChange={handleChange}/>
            </div>
            <button onClick={handleSubmit} className="newScanButton" >Start Scan</button>
        </div>
      </form>
    </div>
    )
}

export default Startscan
