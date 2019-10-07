import React, { Component } from 'react';
import 'font-awesome/css/font-awesome.min.css';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import GroupEdit from './GroupEdit';

class App extends Component {
  render() {
    return (
        <Router>
          <Switch>
            <Route path='/' exact={true} component={Home}/>
            <Route path='/groups/:id' component={GroupEdit}/>
          </Switch>
        </Router>
    )
  }
}

export default App;