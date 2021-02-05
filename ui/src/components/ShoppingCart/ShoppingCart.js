import React, {Component} from 'react';
import {Label} from "semantic-ui-react";
import {connect} from 'react-redux';
import { withRouter } from 'react-router';
import yellowBasket from './yellow_basket_2.png';
import blackBasket from './black_basket_2.png';
import './ShoppingCart.css';


class ShoppingCart extends Component {

    render() {
        let label = null;
        if (this.props.productsToOrder.length !== 0) {
            label = <Label color='red' size='mini' className="ShoppingCart-div-label">
                {this.props.productsToOrder.length}
            </Label>
        }


        let icon = <img src={yellowBasket}
                        alt="naybxrz shopping cart"
                        onClick={this.props.show}
                        className="ShoppingCart-div-icon"/>

        if (this.props.history.location.pathname === '/men'
            || this.props.history.location.pathname === '/women'
            || this.props.history.location.pathname === '/checkout'
            || this.props.history.location.pathname === '/orders'
            || this.props.history.location.pathname === '/userAccount'
            || this.props.history.location.pathname.startsWith('/products/')
        ){
            icon = <img src={blackBasket}
                        alt="naybxrz shopping cart"
                        onClick={this.props.show}
                        className="ShoppingCart-div-icon"/>
        }

        return (
            <div>
                {icon}
                {label}
            </div>
        );
    }

}

const mapStateToProps = state => {
    return {
        productsToOrder: state.productsToOrder.productsToOrder
    }
}

const ShoppingCartWithRouter = withRouter(ShoppingCart);

export default connect(mapStateToProps)(ShoppingCartWithRouter);