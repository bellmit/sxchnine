import React, {Component} from 'react';
import {Grid, Image, Label, Input} from "semantic-ui-react";
import { connect } from 'react-redux';
import './Orders.css';
import OrderPlaceBanner from "../../components/Banner/Banner";
import Card from './Card';
import Contact from '../Contact/Contact';

class Orders extends Component {

    state = {
        open: false,
        total: 0
    }

    componentDidMount(): void {
        this.setState({total: this.props.productsToOrder.map(p => p.price).reduce((p1, p2) => p1 + p2, 0)});
    }

    handleOrder = () => {
        console.log("handle Order ");
    }


    render() {

        return (
            <div>
                <div className="Orders-Yellow-bar-div" />
                <header>
                    <OrderPlaceBanner {...this.props}/>
                </header>
                <div className="Orders-Bag-Resume">
                <span className="Orders-Resume-Text">Your Bag : </span>
                </div>
                <div className="Orders-Resume">
                    <Grid columns={2} centered>
                        {this.props.productsToOrder.map(product => (
                            <Grid.Row centered key={product.id + product.size}>
                                <Grid.Column width={3}>
                                    <Image src={product.image} size='small' circular />
                                </Grid.Column>
                                <Grid.Column width={3}>
                                    <span className="Orders-Items-Text-Header">{product.name}</span>
                                    <p className="Orders-Items-Text">{product.color} </p>
                                    <p className="Orders-Items-Text">{product.size}</p>
                                    <p className="Orders-Items-Text">${product.size}</p>
                                </Grid.Column>
                            </Grid.Row>
                        ))}
                        <Grid.Row>
                            <Grid.Column width={3} height={10}>
                                <p className="Orders-Total-Text">TOTAL:</p>
                            </Grid.Column>
                            <Grid.Column width={3}>
                                <Label tag color='red'>${this.state.total}</Label>
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <span className="Orders-Yellow-second-bar-div" />
                        </Grid.Row>
                    </Grid>
                    <Grid centered>
                        <Grid.Row>
                            <Grid.Column width={3}>
                                <p className="Orders-Email-Text">EMAIL ADDRESS:</p>
                            </Grid.Column>

                            <Grid.Column width={3}>
                                <Input  inverted placeholder='email address...'
                                />
                            </Grid.Column>
                        </Grid.Row>

                        <Grid.Row>
                            <Grid.Column width={3}>
                                <p className="Orders-Email-Text">DELIVERY ADDRESS:</p>
                            </Grid.Column>

                            <Grid.Column width={3}>
                                <Input  inverted placeholder='N°'/>
                                <Input  inverted placeholder='street/avenue'/>
                                <Input  inverted placeholder='city'/>
                                <Input  inverted placeholder='postal code'/>
                                <Input  inverted placeholder='country'/>
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <span className="Orders-Yellow-second-bar-div" />
                        </Grid.Row>
                    </Grid>
                    <Card {...this.props}/>
                    <Contact />
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

export default connect(mapStateToProps)(Orders);