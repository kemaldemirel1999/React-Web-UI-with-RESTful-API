import React from 'react'

import { Route, Switch } from 'react-router-dom'

import Dashboard from '../pages/Dashboard'
import Scans from '../pages/Scans'
import Settings from '../pages/Settings'
import Startscan from '../pages/Startscan'
import Documentation from '../pages/Documentation'
import Results from '../pages/Results'


const Routes = () => {
    return (
        <Switch>
            <Route path='/' exact component={Dashboard}/>
            <Route path='/scans' component={Scans}/>
            <Route path='/settings' component={Settings}/>
            <Route path='/startscan' component={Startscan}/>
            <Route path='/documentation' component={Documentation}/>
            <Route path='/results' component={Results}/>
        </Switch>
    )
}

export default Routes
