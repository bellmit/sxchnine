import React from 'react';
import './Gallery.css';

const Gallery = ( props ) => {
    return (
        <div>
            <img alt="" className="Gallery-Div" src={props.url}/>
        </div>
    );
}

export default Gallery;