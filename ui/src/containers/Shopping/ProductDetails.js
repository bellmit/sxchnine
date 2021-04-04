import React, {PureComponent} from 'react';
import {Button, Dropdown, Form, Grid, Icon, Label} from 'semantic-ui-react';
import {Badge, CSSReset, ThemeProvider} from "@chakra-ui/core";
import {connect} from 'react-redux';
import * as actions from '../../store/actions/index';
import './ProductDetails.css';

class ProductDetails extends PureComponent {
    state = {
        availability: true,
        size: '',
        color: '',
        colors: [],
        availableSize: [],

    };

    componentDidMount(): void {
        this.createColors();
    }

    componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>, snapshot: SS): void {
        if (prevProps.product.colors !== this.props.product.colors) {
            this.createColors();
        }

    }

    handleChangeSize = (e, {value}) => {
        this.setState({value, size: value});
    }

    handleChangeColor = (e, {value}) => {
        this.setState({value, color: value});
        this.setState({availableSize: this.props.product.availability[value].filter(size => size.qte !== 0).map(size => size.size)});
    };

    createColors = () => {
        let options = [];
        this.props.product.colors.forEach(c => {
            options.push({
                key: c, text: c, value: c
            })
        });

        this.setState({colors: options});
    };


    handleAddToOrder = () => {
        const productToOrder = {
            productId: this.props.product.id,
            productName: this.props.product.name,
            productBrand: this.props.product.brand,
            unitPrice: this.props.product.price,
            productSize: this.state.size,
            productColor: this.state.color,
            image: this.props.product.images[0],
            productQte: 1
        }

        this.props.addProductToOrder(productToOrder);
    }

    disableOrder = (color, size) => {
        return color === '' || size === '';
    }

    render() {
        let badge = null;

        if (this.props.product.available) {
            badge = (
                <div>
                    <ThemeProvider>
                        <CSSReset/>
                        <Badge rounded="full" px="2" variantColor="teal">
                            Available
                        </Badge>
                    </ThemeProvider>
                </div>
            )
        } else {
            badge = (
                <div>
                    <ThemeProvider>
                        <CSSReset/>
                        <Badge rounded="full" px="2" variantColor="red">
                            Soldout
                        </Badge>
                    </ThemeProvider>
                </div>
            )
        }

        return (
            <div>
                <p className="Product-Name-Div">{this.props.product.name}</p>
                <div className="Product-Form-Div">
                    <Form unstackable widths='equal' size='large'>

                        <Form.Group inline widths='equal'>
                            <span className="Product-Props-Div">COLOR:&nbsp;&nbsp;&nbsp;</span>
                            <Dropdown className="Product-Dropdown-Text"
                                      onChange={this.handleChangeColor}
                                      fluid
                                      options={this.state.colors}
                                      placeholder='Color'
                                      selection
                                      value={this.state.color}/>
                        </Form.Group>
                        <Form.Group inline widths='equal'>
                            <span className="Product-Props-Div">SIZE:&nbsp;&nbsp;&nbsp;</span>
                            <Form.Radio className="Product-Radio-Text"
                                        label='Small'
                                        value='S'
                                        disabled={!this.state.availableSize.includes('S')}
                                        checked={this.state.value === 'S'}
                                        onChange={this.handleChangeSize}
                            />
                            <Form.Radio className="Product-Radio-Text"
                                        label='Medium'
                                        value='M'
                                        disabled={!this.state.availableSize.includes('M')}
                                        checked={this.state.value === 'M'}
                                        onChange={this.handleChangeSize}
                            />
                            <Form.Radio className="Product-Radio-Text"
                                        label='Large'
                                        value='L'
                                        disabled={!this.state.availableSize.includes('L')}
                                        checked={this.state.value === 'L'}
                                        onChange={this.handleChangeSize}
                            />
                            <Form.Radio className="Product-Radio-Text"
                                        label='XLarge'
                                        value='XL'
                                        disabled={!this.state.availableSize.includes('XL')}
                                        checked={this.state.value === 'XL'}
                                        onChange={this.handleChangeSize}
                            />
                        </Form.Group>
                        <Form.Group inline widths='equal'>
                            <span className="Product-Props-Div">PRICE:&nbsp;&nbsp;&nbsp;</span>
                            <Label tag color='red'>
                                ${this.props.product.price}
                            </Label>
                            <span className="Product-Taxes-Div">&nbsp;&nbsp;&nbsp;Price include taxes</span>
                        </Form.Group>
                        <Form.Group>
                            <a href="/shipping" className="Product-Delivery-Div">&nbsp;&nbsp;&nbsp;Delivery & return
                                info</a>
                        </Form.Group>
                        <Form.Group>
                            {badge}
                        </Form.Group>
                        <Button animated='vertical' inverted color='yellow' floated='right'
                                onClick={this.handleAddToOrder}
                                disabled={this.disableOrder(this.state.color, this.state.size)}>
                            <Button.Content hidden>Got it!</Button.Content>
                            <Button.Content visible>
                                <Icon name='shop' inverted color='yellow'/>
                            </Button.Content>
                        </Button>
                    </Form>
                </div>

                <div className="Grid-Container-Div">
                    <Grid columns={1} verticalAlign='bottom'>
                        <Grid.Row stretched>
                            <Grid.Column>
                                <p className="Product-Grid-Div">PRODUCT DETAILS:</p>
                                <p className="Product-Grid-Div">100% COTTON</p>
                            </Grid.Column>
                        </Grid.Row>

                        <Grid.Row stretched>
                            <Grid.Column>
                                <p className="Product-Grid-Div">MODEL DETAILS:</p>
                                <p className="Product-Grid-Div">HEIGHT 170cm/79</p>
                                <p className="Product-Grid-Div">SIZE: S</p>

                            </Grid.Column>
                        </Grid.Row>
                    </Grid>
                </div>

            </div>
        );
    }
}

const dispatchToProps = dispatch => {
    return {
        addProductToOrder: (productToOrder) => dispatch(actions.addProductToOrder(productToOrder))
    }
};

export default connect(null, dispatchToProps)(ProductDetails);