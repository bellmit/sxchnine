import React, {PureComponent} from "react";
import {connect} from 'react-redux';
import {Button, Form, Icon, Image, Modal, Segment} from "semantic-ui-react";
import * as actions from "../../../../store/actions";
import './EditProduct.css';

class EditProduct extends PureComponent {

    state = {
        ref: this.props.productByIdData.ref,
        name: this.props.productByIdData.name,
        sex: this.props.productByIdData.sex,
        brand: this.props.productByIdData.brand,
        logo: this.props.productByIdData.logo,
        category: this.props.productByIdData.category,
        price: this.props.productByIdData.price,
        size: this.props.productByIdData.size,
        colors: this.props.productByIdData.colors,
        images: this.props.productByIdData.images,
        availability: this.props.productByIdData.availability,
        available: this.props.productByIdData.available,
        quantity: this.props.productByIdData.quantity,
        dimension: this.props.productByIdData.dimension,
        originalPrice: this.props.productByIdData.originalPrice,
        promotion: this.props.productByIdData.promotion,
        store: this.props.productByIdData.store,
        dateTime: this.props.productByIdData.dateTime,
    }

    refreshState = () => {
        this.setState({
            ref: this.props.productByIdData.ref,
            name: this.props.productByIdData.name,
            sex: this.props.productByIdData.sex,
            brand: this.props.productByIdData.brand,
            logo: this.props.productByIdData.logo,
            category: this.props.productByIdData.category,
            price: this.props.productByIdData.price,
            size: this.props.productByIdData.size,
            colors: this.props.productByIdData.colors,
            images: this.props.productByIdData.images,
            availability: this.props.productByIdData.availability,
            available: this.props.productByIdData.available,
            quantity: this.props.productByIdData.quantity,
            dimension: this.props.productByIdData.dimension,
            originalPrice: this.props.productByIdData.originalPrice,
            promotion: this.props.productByIdData.promotion,
            store: this.props.productByIdData.store,
            dateTime: this.props.productByIdData.dateTime
        });
    }

    handleChange = (e, {name, value}) => this.setState({[name]: value});

    handleChangeSex = (e, {value}) => this.setState({value, sex: value});

    closeModal = () => this.props.closeProductByIdPopup(false);


    render() {
        let sexOptions = [
            {key: 0, text: '----', value: ''},
            {key: 1, text: 'Men', value: 'M'},
            {key: 2, text: 'Women', value: 'W'},
        ]

        return (
            <Modal open={this.props.productByIdPopup}
                   onClose={this.closeModal}
                   size='large' onOpen={this.refreshState} onMount={this.refreshState}>
                <Modal.Content>
                    <Segment.Group>
                        <Segment>
                            <Icon name='archive'/>
                            <span style={{fontWeight: 'bold'}}>Product Info</span>
                        </Segment>
                        <Segment.Group>
                            <Segment>
                                <Form>
                                    <Form.Group widths='equal'>
                                        <Form.Input fluid
                                                    label='Product ID:'
                                                    name='id'
                                                    value={this.props.productByIdData.id} disabled inverted/>
                                        <Form.Input fluid
                                                    label='Date Time:'
                                                    name='dateTime'
                                                    value={this.props.productByIdData.dateTime} disabled/>
                                        <Image avatar size='small' src={this.props.productByIdData.logo}/>
                                    </Form.Group>
                                    <Form.Group widths='equal'>
                                        <Form.Input fluid
                                                    label='Ref:'
                                                    name='ref'
                                                    value={this.state.ref}/>
                                        <Form.Input fluid
                                                    label='Name:'
                                                    name='name'
                                                    value={this.state.name}/>
                                    </Form.Group>
                                </Form>
                            </Segment>
                        </Segment.Group>

                        <Segment>
                            <Icon name='barcode'/>
                            <span style={{fontWeight: 'bold'}}>Product Details</span>
                        </Segment>
                        <Segment.Group>
                            <Segment>
                                <Form>
                                    <Form.Group widths='equal' inline>
                                        <Form.Dropdown onChange={this.handleChangeSex}
                                                       placeholder='Sex:'
                                                       text={this.state.text}
                                                       label='Sex'
                                                       options={sexOptions}
                                                       selection
                                                       value={this.state.sex}/>
                                        <Form.Input fluid
                                                    label='Category:'
                                                    name='category'
                                                    value={this.state.category}/>
                                        <Form.Input fluid
                                                    label='Store:'
                                                    name='store'
                                                    value={this.state.store}/>

                                    </Form.Group>

                                    <Form.Group widths='equal' inline>
                                        <Form.Input fluid
                                                    label='Brand:'
                                                    name='brand'
                                                    value={this.state.brand}/>
                                        <Form.Input fluid
                                                    label='URL logo:'
                                                    name='logo'
                                                    icon='linkify'
                                                    value={this.state.logo}/>
                                    </Form.Group>
                                    {this.state.images.map((image, idx) => (
                                        <Form.Group key={idx} inline>
                                            <Image size='small' src={image} />
                                        </Form.Group>
                                    ))}
                                </Form>
                            </Segment>
                        </Segment.Group>
                    </Segment.Group>

                    <Modal.Actions>
                        <Button className="order-save-button"
                                color='black'
                                floated='right'
                                onClick={this.saveCurrentOrder}>
                            <span className="order-save-button-text">SAVE</span>
                            <Icon name='right chevron' color='yellow'/>
                        </Button>
                    </Modal.Actions>
                </Modal.Content>
            </Modal>
        );
    }
}

const mapStateToProps = state => {
    return {
        productByIdPopup: state.products.productByIdPopup,
        productByIdData: state.products.productByIdData,
        saveOrderError: state.products.saveOrderError
    }
}

const dispatchToProps = dispatch => {
    return {
        getProductById: (productId) => dispatch(actions.getProductById(productId)),
        closeProductByIdPopup: (open) => dispatch(actions.productByIdPopup(open)),
        saveProduct: (product) => dispatch(actions.saveOrder(product))
    }
}


export default connect(mapStateToProps, dispatchToProps)(EditProduct);