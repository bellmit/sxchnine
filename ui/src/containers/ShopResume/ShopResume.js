import React, {Component} from "react";
import {Button, Header, Icon, Image, Modal} from "semantic-ui-react";
import ShoppingCart from '../../components/ShoppingCart/ShoppingCart';
import { connect } from 'react-redux';
import * as actions from '../../store/actions/index';
import './ShopResume.css';
import panierPic from './image80s.jpeg';
import trash from './trash-10-48.png';

class ShopResume extends Component {

    state = {
        open: false,
    }


    show = (size) => () => this.setState({size, open: true})
    close = () => this.setState({open: false})

    redirectToOrders = () => {
        this.props.history.push('/checkout');
    };

    removeProduct = (id) => {
        this.props.removeProductToOrder(id);
    };


    render() {
        const {open, size} = this.state;

        return (
            <div>
                <div>
                    <ShoppingCart show={this.show('small')}/>
                </div>

                <div className="Modal-Content-div">
                    <Modal size={size} open={open} onClose={this.close}
                           style={{position: 'static', height: 'auto'}}>
                        <Modal.Header><Image src={panierPic} fluid style={{height: '220px'}}/></Modal.Header>
                        {/*
                    <span className="Panier-Resume-Text"> You GOT : </span>
*/}

                        {this.props.productsToOrder.map((product, index) => (
                            <Modal.Content image key={index}>
                                <Image wrapped size='small'
                                       src={product.image}/>
                                <Modal.Description>
                                    <Header>
                                        <span className="Panier-Items-Text-Header">{product.name}</span>
                                        <img alt="" src={trash}
                                             className="Trash-Icon"
                                             onClick={() => this.removeProduct(index)}/>
                                    </Header>
                                    <p className="Panier-Items-Text">{product.color}</p>
                                    <p className="Panier-Items-Text">{product.size}</p>
                                    <p className="Panier-Items-Text">${product.price}</p>
                                </Modal.Description>
                            </Modal.Content>
                        ))}

                        <Modal.Actions>
                            <Button color='black' onClick={this.redirectToOrders} disabled={this.props.productsToOrder.length === 0}>
                                <span className="Pay-Text">CHECKOUT</span><Icon name='right chevron' color='yellow'/>
                            </Button>
                        </Modal.Actions>
                    </Modal>
                </div>
            </div>
        );
    }

}

const mapStateToProps = state => {
    return {
        productsToOrder: state.productsToOrder.productsToOrder
    }
}

const dispatchToProps = dispatch => {
    return {
        removeProductToOrder: (id) => dispatch(actions.removeProductToOrder(id))
    }
}

export default connect(mapStateToProps, dispatchToProps)(ShopResume);