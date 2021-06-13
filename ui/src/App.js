import React, {useEffect} from 'react';
import {Route} from 'react-router-dom';
import './App.css';
import Logo from './components/Logo/Logo';
import ReactGA from "react-ga";
import Connexion from "./containers/Connexion/Connexion";
import Menu from "./containers/Menu/Menu";
import ShopResume from "./containers/ShopResume/ShopResume";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import Home from "./components/Home/Home";
import Products from "./containers/Products/Products";
import ProductSlick from "./containers/Shopping/ProductSlick";
import Checkout from "./containers/Checkout/Checkout";
import Orders from "./containers/Payment/Orders";
import Processing from "./containers/Payment/Processing";
import Confirmation from "./components/Confirmation/Confirmation";
import Shipping from "./components/Shipping/Shipping";
import ContactUs from "./containers/ContactUs/ContactUs";
import ContactUsSent from "./containers/ContactUs/ContactUsSent";
import CustomerService from "./containers/CustomerService/CustomerService";
import Tracking from "./containers/Tracking/Tracking";
import OurStory from "./components/Story/OurStory";
import UserAccount from "./containers/Connexion/UserAccount";
import Subscription from "./containers/Subscription/Subscription";


function App() {

    useEffect(() => {
        ReactGA.initialize(process.env.REACT_APP_TRACKER);
        ReactGA.pageview(window.location.pathname + window.location.search);
    }, [])

    return (
        <div className="App-Container">

            <Logo/>
            <Menu />
            <Connexion />
            <ShopResume />
            <Route path="/" exact component={Home}/>
            <Route path="/checkout" exact component={ShopResume}/>
            <Route path="/men" exact component={Products}/>
            <Route path="/women" exact component={Products}/>
            <Route path="/products/:productId" exact component={ProductSlick}/>
            <Route path="/checkout" exact component={Checkout}/>
            <Route path="/orders" component={Orders}/>
            <Route path="/processing" component={Processing}/>
            <Route path="/confirmation/:status" exact component={Confirmation}/>
            <Route path="/shipping" exact component={Shipping}/>
            <Route path="/contactUs" exact component={ContactUs}/>
            <Route path="/contactSent" exact component={ContactUsSent}/>
            <Route path="/customer" exact component={CustomerService}/>
            <Route path="/tracking" exact component={Tracking}/>
            <Route path="/ourStory" exact component={OurStory}/>
            <Route path="/subscription" exact component={Subscription}/>

            <PrivateRoute path="/userAccount" exact component={UserAccount}/>
        </div>
    );
}

export default App;
