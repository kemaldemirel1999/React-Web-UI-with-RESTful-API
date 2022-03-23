import React, {useEffect} from 'react'

import { useSelector } from 'react-redux'

import StatusCard from '../components/status-card/StatusCard'

import Table from '../components/table/Table'

import Badge from '../components/badge/Badge'

import statusCards from '../assets/JsonData/status-card-data.json'

import lastScanList from '../assets/JsonData/last-scan-list.json'

const renderHead = (item, index) => <th key={index}>{item}</th>

const latestScanHead = [
    'ID',
    'Scan Type',
    'Target URL',
    'Status'
]
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
const orderStatus = {
    "Continuing": "warning",
    "Finished": "success",
    "Failed": "danger"
}

const renderOrderHead = (item, index) => (
    <th key={index}>{item}</th>
)

const renderOrderBody = (item, index) => (
    <tr key={index}>
        <td>{item.id}</td>
        <td>{item.scantype}</td>
        <td>{item.targeturl}</td>
        <td>
            <Badge type={orderStatus[item.status]} content={item.status}/>
        </td>
    </tr>
)

const Dashboard = () => {

    const themeReducer = useSelector(state => state.ThemeReducer.mode)

    return (
        <div>
            <h2 className="page-header">Homepage</h2>
            <div className="row">
                <div className="col-6">
                    <div className="row">
                        {
                            statusCards.map((item, index) => (
                                <div className="col-4" key={index}>
                                    <StatusCard
                                        icon={item.icon}
                                        count={item.count}
                                        title={item.title}
                                    />
                                </div>
                            ))
                        }
                    </div>
                </div>
                
                <div className="col-8">
                    <div className="card">
                        <div className="card__header">
                            <h3>Latest Scan</h3>
                        </div>
                        <div className="card__body">
                            <Table
                                headData={latestScanHead}
                                renderHead={(item, index) => renderHead(item, index)}
                                bodyData={lastScanList}
                                renderBody={(item, index) => renderBody(item, index)}
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Dashboard
