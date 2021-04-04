import React, {useEffect} from 'react';
import {Route} from 'react-router-dom';
import './App.css';
import Logo from './components/Logo/Logo';
import Subscription from "./containers/Subscription/Subscription";
import ReactGA from "react-ga";


function App() {

    useEffect(() => {
        ReactGA.initialize(process.env.REACT_APP_TRACKER);
        ReactGA.pageview(window.location.pathname + window.location.search);
    }, [])

    return (
        <div className="App-Container">

            <Route path="/" exact component={Subscription}/>
            <Logo/>

            {/*        <Menu />
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

        <PrivateRoute  path="/userAccount" exact component={UserAccount}/>*/}
        </div>
    );
}

export default App;
