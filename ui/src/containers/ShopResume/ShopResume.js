import React, {Component} from "react";
import {Button, Header, Icon, Image, Label, Modal} from "semantic-ui-react";
import { withRouter } from 'react-router';
import ShoppingCart from '../../components/ShoppingCart/ShoppingCart';
import { connect } from 'react-redux';
import * as actions from '../../store/actions/index';
import './ShopResume.css';
import gameon from './gameon.jpg';
import trash from './trash-10-48.png';

class ShopResume extends Component {

    state = {
        open: false,
    };


    show = (size) => () => this.setState({size, open: true})
    close = () => this.setState({open: false})

    redirectToOrders = () => {
        if (this.props.userAuthenticated !== ''){
            this.props.history.push('/orders');
        } else {
            this.props.history.push('/checkout');
        }
        this.setState({open: false});
    };

    removeProduct = (id) => {
        this.props.removeProductToOrder(id);
    };


    render() {
        const {open, size} = this.state;

        let messageNoItems = undefined;
        if (this.props.productsToOrder.length === 0){
            messageNoItems = <Label color="red"
                                    attached="center"
                                    className="Message-No-Items">No items ... Go get it !</Label>
        }

        return (
            <div>
                <div>
                    <ShoppingCart {...this.props} show={this.show('small')}/>
                </div>

                <div className="Modal-Content-div">
                    <Modal size={size} open={open} onClose={this.close}
                           style={{position: 'static', height: 'auto'}}>
                        <Modal.Header><Image src={gameon} fluid style={{height: '250px', objectFit: 'cover'}}/></Modal.Header>

                        {this.props.productsToOrder.map((product, index) => (
                            <Modal.Content image key={index}>
                                <Image wrapped size='small'
                                       src={product.image}/>
                                <Modal.Description>
                                    <Header>
                                        <span className="Panier-Items-Text-Header">{product.productName}</span>
                                        <img alt="naybxrz econcept store vintage clothes 90's adidas carhartt obey supreme nike nocta bombers wutang chimodu hip hop culture streetwear lifestyle hoodies shirts hat bob"
                                             src={trash}
                                             className="Trash-Icon"
                                             onClick={() => this.removeProduct(index)}/>
                                    </Header>
                                    <p className="Panier-Items-Text">{product.productColor}</p>
                                    <p className="Panier-Items-Text">{product.productSize}</p>
                                    <p className="Panier-Items-Text">${product.unitPrice}</p>
                                </Modal.Description>
                            </Modal.Content>
                        ))}
                        {messageNoItems}
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
        productsToOrder: state.productsToOrder.productsToOrder,
        userAuthenticated: state.users.userAuthenticated
    }
}

const dispatchToProps = dispatch => {
    return {
        removeProductToOrder: (id) => dispatch(actions.removeProductToOrder(id))
    }
}

const ShopResumeWithRouter = withRouter(ShopResume);
export default connect(mapStateToProps, dispatchToProps)(ShopResumeWithRouter);