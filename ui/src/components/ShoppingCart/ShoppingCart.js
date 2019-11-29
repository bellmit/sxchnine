import React from 'react';
import {Button, Icon, Label} from "semantic-ui-react";
import './ShoppingCart.css';


const shoppingCart = (props) => {

    let label = null;
    if (props.size !== 0){
        label = <Label color='red' floating size='mini'>
            {props.size}
        </Label>
    }
    return (
        <Button circular color='black' className="ShoppingCart-div" onClick={props.show}>
            <Icon name="basket shopping" className="ShoppingCart-div-icon"/>
            {label}
        </Button>
    );
}

export default shoppingCart;