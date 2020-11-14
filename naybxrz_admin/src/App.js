import React from 'react';
import { Route } from 'react-router-dom';
import './App.css';
import Logo from './components/Logo/Logo';
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import Login from './containers/Login/Login';
import Home from './containers/Home/Home';

function App() {
    return (
        <div className="App">
            <Logo />
            <Route path='/' exact component={Login}/>
            <PrivateRoute path='/home' exact component={Home} />
        </div>
    );
}

export default App;
