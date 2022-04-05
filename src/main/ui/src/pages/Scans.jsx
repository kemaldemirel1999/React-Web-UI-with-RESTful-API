import React from 'react'
import Badge from '../components/badge/Badge'
import Table from '../components/table/Table'

import scanList from '../assets/JsonData/scan-list.json'
import {useHistory} from "react-router-dom";

const Scans = () => {


    const scanTableHead = [
        'ID',
        'Scan Type',
        'Target URL',
        'Status',
        'High Risks',
        'Middle Risks',
        'Low Risks'
    ]

    const history = useHistory();


    const renderHead = (item, index) => <th key={index}>{item}</th>

    const orderStatus = {
        "Continuing": "warning",
        "Finished": "success",
        "Failed": "danger"
    }

    const renderBody = (item, index) => (
        <tr key={index}>
            <td>{item.id}</td>
            <td>{item.scanType}</td>
            <td>{item.targetURL}</td>
            <td>
                <Badge type={orderStatus[item.status]} content={item.status}/>
            </td>
            <td>{item.highrisks}</td>
            <td>{item.middlerisks}</td>
            <td>{item.lowrisks}</td>
        </tr>
    )


    const handleDelete = (e) => {
        console.log(e);
    }
    const handleDetails = (e) => {
        console.log("x:"+e);
    }
    return (
        <div>
            <h2 className="page-header">
                Scans
            </h2>
            <div className="row">
                <div className="col-12">
                    <div className="card">
                        <div className="card__body">
                            <Table
                                limit='10'
                                headData={scanTableHead}
                                renderHead={(item, index) => renderHead(item, index)}
                                bodyData={scanList}
                                renderBody={(item, index) => renderBody(item, index)}
                            />
                        </div>

                    </div>

                </div>

            </div>
        </div>
    )
}

export default Scans
