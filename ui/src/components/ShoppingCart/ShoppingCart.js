import React, {Component} from 'react';
import {Button, Icon, Label} from "semantic-ui-react";
import {connect} from 'react-redux';
import './ShoppingCart.css';


class ShoppingCart extends Component {

    render() {
        let label = null;
        /*
            if (props.size !== 0){
        */
        if (this.props.productsToOrder.length !== 0) {
            label = <Label color='red' floating size='mini'>
                {this.props.productsToOrder.length}
            </Label>
        }
        return (
            <Button circular color='black' className="ShoppingCart-div" onClick={this.props.show}>
                <Icon name="basket shopping" className="ShoppingCart-div-icon"/>
                {label}
            </Button>
        );
    }

}

const mapStateToProps = state => {
    return {
        productsToOrder: state.productsToOrder.productsToOrder
    }
}

export default connect(mapStateToProps)(ShoppingCart);