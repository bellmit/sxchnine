import React, {PureComponent} from "react";
import {connect} from 'react-redux';
import {Button, Dimmer, Dropdown, Form, Icon, Image, Label, List, Loader, Modal, Segment} from "semantic-ui-react";
import * as actions from "../../../../store/actions";
import './EditProduct.css';
import trash from './trash.png';

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
        showDelete: true,
        showAddImage: false,
        newImage: '',
        showAddColor: false,
        newColor: ''
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

    handleChangeAvailable = (e, {value}) => this.setState({value, available: value});

    handleChangeSize = (e, {value}) => this.setState({size: value});

    handleChangeColor = (e, {value}) => this.setState({colors: value});

    closeModal = () => this.props.closeProductByIdPopup(false);

    showDeleteButton = () => this.setState({showDelete: !this.state.showDelete});

    showAddImage = () => this.setState({showAddImage: !this.state.showAddImage});

    showAddColor = () => this.setState({showAddColor: !this.state.showAddColor});


    addNewImage = () => {
        let images = [...this.state.images];
        images.push(this.state.newImage);
        this.setState({
            images: images,
            showAddImage: false
        });
    };

    addNewColor = () => {
        let colors = [...this.state.colors];
        colors.push(this.state.newColor);
        this.setState({
            colors: colors,
            showAddColor: false
        });
    }

    removeImage = (id) => {
        let images = [...this.state.images];
        images.splice(id, 1);
        this.setState({images: images});
    }

    checkAvailableIcon() {
        if (this.state.available) {
            return 'green'
        } else {
            return 'red'
        }
    }

    incrementSize(key, size) {
        let availabilityToSave = {...this.state.availability};
        let sizeQt = availabilityToSave[key];
        console.log(sizeQt);
        for (let sq of sizeQt){
            if (sq.size === size){
                sq.qte++;
            }
        }
        availabilityToSave[key] = sizeQt;
        console.log(availabilityToSave)
        this.setState({availability: availabilityToSave});
    }

    decrementSize(key, size) {
        let availabilityToSave = {...this.state.availability};
        let sizeQt = availabilityToSave[key];
        console.log(sizeQt);
        for (let sq of sizeQt){
            if (sq.size === size){
                sq.qte--;
            }
        }
        availabilityToSave[key] = sizeQt;
        this.setState({availability: availabilityToSave});
    }

    saveProduct = () => {
        this.props.saveProduct(this.createProduct());
    }

    createProduct() {
        return {
            id: this.props.productByIdData.id,
            ref: this.state.ref,
            name: this.state.name,
            sex: this.state.sex,
            brand: this.state.brand,
            logo: this.state.logo,
            category: this.state.category,
            price: this.state.price,
            size: this.state.size,
            colors: this.state.colors,
            images: this.state.images,
            availability: this.state.availability,
            available: this.state.available,
            quantity: this.state.quantity,
            dimension: this.state.dimension,
            originalPrice: this.state.originalPrice,
            promotion: this.state.promotion,
            store: this.state.store,
            dateTime: this.props.productByIdData.dateTime
        }
    }


    render() {

        let optionsSize = [
            {key: 1, text: 'Small', value: 'S'},
            {key: 2, text: 'Medium', value: 'M'},
            {key: 3, text: 'Large', value: 'L'},
            {key: 4, text: 'XLarge', value: 'XL'},
        ];

        let colors = [];
        this.state.colors.forEach(c => {
            colors.push({
                key: c, text: c, value: c
            })
        });

        let sexOptions = [
            {key: 0, text: '----', value: ''},
            {key: 1, text: 'Men', value: 'M'},
            {key: 2, text: 'Women', value: 'W'},
        ]

        let availableOptions = [
            {key: 1, text: 'Available', value: true},
            {key: 2, text: 'Sold out', value: false},
        ]

        let formAddImage = undefined;

        if (this.state.showAddImage) {
            formAddImage = <Form>
                <Form.Group inline>
                    <Form.Input width='10' fluid
                                placeholder='New image URL'
                                name='newImage'
                                icon='linkify'
                                value={this.state.newImage}
                                onChange={this.handleChange}
                    />
                    <Icon name="add"
                          color='green'
                          size='big'
                          className="product-delete-button"
                          onClick={this.addNewImage}/>
                </Form.Group>
            </Form>

        }


        let formAddColor = undefined;

        if (this.state.showAddColor) {
            formAddColor =
                <Form.Group inline>
                    <Form.Input width='10' fluid
                                placeholder='New Color'
                                name='newColor'
                                value={this.state.newColor}
                                onChange={this.handleChange}
                    />
                    <Icon name="add"
                          color='green'
                          size='big'
                          className="product-delete-button"
                          onClick={this.addNewColor}/>
                </Form.Group>
        }

        let availabilityBlock = undefined;
        for (var key in this.state.availability) {
            availabilityBlock =
                <Form.Group inline>
                    <Label size='small'
                        basic
                        color='green'>Color:</Label>
                    <span style={{margin: '0 10% 0 1%', fontWeight: 'bold'}}>{key}</span>
                    {this.state.availability[key].map((av, indV) => (
                        <List key={indV} horizontal relaxed>
                            <List.Item>
                                <Label size='small'
                                    basic
                                    color='green'>Size:</Label>
                            </List.Item>
                            <List.Item>
                                <span style={{fontWeight: 'bold'}}>{av.size}</span>
                            </List.Item>
                            <List.Item>
                                <span>{av.qte}</span>
                            </List.Item>
                            <List.Item>
                                <Button circular icon='add' size='small' color='green' onClick={() => this.incrementSize(key, av.size)}/>
                                <Button circular icon='delete' size='small' color='red' onClick={() => this.decrementSize(key, av.size)}/>
                            </List.Item>
                        </List>
                    ))}
                </Form.Group>
        }


        return (
            <Modal open={this.props.productByIdPopup}
                   onClose={this.closeModal}
                   size='large' onOpen={this.refreshState} onMount={this.refreshState}>
                <Dimmer active={this.props.saveProductLoading} page>
                    <Loader content='Loading'/>
                </Dimmer>
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
                                                    value={this.props.productByIdData.id}
                                                    disabled
                                                    inverted/>
                                        <Form.Input fluid
                                                    label='Date Time:'
                                                    name='dateTime'
                                                    value={this.props.productByIdData.dateTime}
                                                    disabled/>
                                        <Form.Dropdown options={availableOptions}
                                                       width='3'
                                                       style={{
                                                           fontWeight: 'bold',
                                                           color: this.checkAvailableIcon()
                                                       }}
                                                       name='available'
                                                       value={this.state.available}
                                                       onChange={this.handleChangeAvailable}/>
                                        <Image avatar size='small' src={this.props.productByIdData.logo}/>
                                    </Form.Group>
                                    <Form.Group widths='equal'>
                                        <Form.Input fluid
                                                    label='Ref:'
                                                    name='ref'
                                                    value={this.state.ref}
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='Name:'
                                                    name='name'
                                                    value={this.state.name}
                                                    onChange={this.handleChange}/>
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
                                                       label='Sex:'
                                                       options={sexOptions}
                                                       selection
                                                       value={this.state.sex}/>
                                        <Form.Input fluid
                                                    label='Category:'
                                                    name='category'
                                                    value={this.state.category}
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='Store:'
                                                    name='store'
                                                    value={this.state.store}
                                                    onChange={this.handleChange}/>

                                    </Form.Group>
                                    <Form.Group inline widths='equal'>
                                        <Form.Input
                                            icon='dollar'
                                            label='Price:'
                                            name='price'
                                            value={this.state.price}
                                            onChange={this.handleChange}/>
                                        <Form.Input
                                            icon='dollar'
                                            label='Original Price:'
                                            name='originalPrice'
                                            value={this.state.originalPrice}
                                            onChange={this.handleChange}/>

                                        <Form.Input
                                            icon='tag'
                                            label='Promotion:'
                                            name='promotion'
                                            value={this.state.promotion}
                                            onChange={this.handleChange}/>

                                    </Form.Group>

                                    <Form.Group widths='equal' inline>
                                        <Form.Input fluid
                                                    label='Brand:'
                                                    name='brand'
                                                    value={this.state.brand}
                                                    onChange={this.handleChange}/>
                                        <Form.Input fluid
                                                    label='URL logo:'
                                                    name='logo'
                                                    icon='linkify'
                                                    value={this.state.logo}
                                                    onChange={this.handleChange}/>
                                    </Form.Group>
                                    <Icon name='add'
                                          size='large'
                                          color='green'
                                          className="product-delete-button"
                                          onClick={this.showAddImage}/>
                                    <Icon name='delete'
                                          color='red'
                                          size='large'
                                          className="product-delete-button"
                                          onClick={this.showDeleteButton}/>
                                    {formAddImage}
                                    {this.state.images.map((image, idx) => (
                                        <Form.Group key={idx} inline>
                                            <Image size='small'
                                                   src={image}/>
                                            <Image size='mini'
                                                   src={trash}
                                                   className="product-delete-button"
                                                   disabled={this.state.showDelete}
                                                   onClick={() => this.removeImage(idx)}/>
                                        </Form.Group>
                                    ))}

                                    <Segment>
                                        <Icon name='shopping bag'/>
                                        <span style={{fontWeight: 'bold'}}>Availability</span>

                                        <Form.Group>
                                            <Dropdown
                                                placeholder='Size'
                                                fluid
                                                multiple
                                                selection
                                                options={optionsSize}
                                                value={this.state.size}
                                                onChange={this.handleChangeSize}/>
                                            <Dropdown
                                                placeholder='Colors'
                                                fluid
                                                multiple
                                                selection
                                                options={colors}
                                                value={this.state.colors}
                                                onChange={this.handleChangeColor}/>
                                            <Icon className="product-delete-button"
                                                  name='add'
                                                  color='green'
                                                  size='big'
                                                  onClick={this.showAddColor}/>
                                        </Form.Group>
                                        {formAddColor}
                                        {availabilityBlock}
                                    </Segment>
                                </Form>
                            </Segment>
                        </Segment.Group>
                    </Segment.Group>

                    <Modal.Actions>
                        <Button className="product-save-button"
                                color='black'
                                floated='right'
                                onClick={this.saveProduct}>
                            <span className="product-save-button-text">SAVE</span>
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
        saveProductError: state.products.saveProductError,
        saveProductLoading: state.products.saveProductLoading
    }
}

const dispatchToProps = dispatch => {
    return {
        getProductById: (productId) => dispatch(actions.getProductById(productId)),
        closeProductByIdPopup: (open) => dispatch(actions.productByIdPopup(open)),
        saveProduct: (product) => dispatch(actions.saveProduct(product))
    }
}


export default connect(mapStateToProps, dispatchToProps)(EditProduct);