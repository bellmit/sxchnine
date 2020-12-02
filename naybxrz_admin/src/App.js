import React from 'react';
import { Route } from 'react-router-dom';
import './App.css';
import Logo from './components/Logo/Logo';
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import Login from './containers/Login/Login';
import Home from './containers/Home/Home';
import ManageOrders from "./containers/Orders/Manage/ManageOrders";
import Welcome from "./containers/Welcome/Welcome";
import AdminMenu from "./components/Menu/Menu";
import ManageProducts from "./containers/Products/Manage/ManageProducts";
import EditOrder from "./containers/Orders/Manage/EditOrder/EditOrder";
import EditProduct from "./containers/Products/Manage/EditProduct/EditProduct";
import AddProduct from "./containers/Products/Manage/AddProduct/AddProduct";

function App() {
    return (
        <div className="App">
            <Logo />
            <Route path='/' exact component={Login}/>
            <PrivateRoute path='/:a([A-Za-z]+)'  component={Welcome} />
            <PrivateRoute path='/:a([A-Za-z]+)'  component={AdminMenu} />
            <PrivateRoute path='/home' exact component={Home} />
            <PrivateRoute path='/manageOrders' exact component={ManageOrders} />
            <PrivateRoute path="/order/:orderId" exact component={EditOrder} />
            <PrivateRoute path='/manageProducts' exact component={ManageProducts} />
            <PrivateRoute path='/product/:productId' exact component={EditProduct} />
            <PrivateRoute path='/product/new' exact component={AddProduct} />
        </div>
    );
}

export default App;
