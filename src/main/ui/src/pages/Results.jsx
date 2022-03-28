import React from 'react'
import Badge from '../components/badge/Badge'
import Table from '../components/table/Table'

import resultList from '../assets/JsonData/result-list.json'


const renderHead = (item, index) => <th key={index}>{item}</th>


const renderBody = (item, index) => (
    <tr key={index}>
        <td>{item.result}</td>
    </tr>
)

const Results = () => {
    return (
        <div>
            <h2 className="page-header">
                Result
            </h2>
            <div className="row">
                <div className="col-12">
                    <div className="card">
                        <div className="card__body">
                            <Table
                                limit='10'
                                bodyData={resultList}
                                renderBody={(item, index) => renderBody(item, index)}
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Results
