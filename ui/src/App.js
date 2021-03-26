import React from 'react';
import { Route } from 'react-router-dom';
import './App.css';
import Menu from './containers/Menu/Menu';
import Logo from './components/Logo/Logo';
import Home from './components/Home/Home';
import Products from './containers/Products/Products';
import ProductSlick from './containers/Shopping/ProductSlick';
import Checkout from './containers/Checkout/Checkout';
import Orders from "./containers/Payment/Orders";
import Confirmation from './components/Confirmation/Confirmation';
import Shipping from './components/Shipping/Shipping';
import ContactUs from "./containers/ContactUs/ContactUs";
import ContactUsSent from "./containers/ContactUs/ContactUsSent";
import CustomerService from "./containers/CustomerService/CustomerService.js";
import Tracking from "./containers/Tracking/Tracking";
import Connexion from "./containers/Connexion/Connexion";
import UserAccount from "./containers/Connexion/UserAccount";
import Processing from "./containers/Payment/Processing";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import OurStory from "./components/Story/OurStory";
import Subscription from "./containers/Subscription/Subscription";
import ShopResume from "./containers/ShopResume/ShopResume";

function App() {
    return (
    <div className="App-Container">
        <Menu />
        <Logo />
        <Connexion />
        <ShopResume />
        <Route  path="/" exact component={Home}/>
        <Route  path="/checkout" exact component={ShopResume}/>
        <Route  path="/men" exact component={Products}/>
        <Route  path="/women" exact component={Products}/>
        <Route  path="/products/:productId" exact component={ProductSlick}/>
        <Route  path="/checkout" exact component={Checkout}/>
        <Route  path="/orders" component={Orders} />
        <Route  path="/processing" component={Processing} />
        <Route  path="/confirmation/:status" exact component={Confirmation} />
        <Route  path="/shipping" exact component={Shipping} />
        <Route  path="/contactUs" exact component={ContactUs} />
        <Route  path="/contactSent" exact component={ContactUsSent} />
        <Route  path="/customer" exact component={CustomerService} />
        <Route  path="/tracking" exact component={Tracking} />
        <Route  path="/ourStory" exact component={OurStory} />
        <Route  path="/subscription" exact component={Subscription} />

        <PrivateRoute  path="/userAccount" exact component={UserAccount}/>
    </div>
  );
}

export default App;
