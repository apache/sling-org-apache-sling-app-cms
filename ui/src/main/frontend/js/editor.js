/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

if (!window.CMSEditor) {
  const { Sling } = window;
  let mouseX;
  let mouseY;
  const CMSEditor = {
    draggable: null,
    init() {
      CMSEditor.util.attachEvents(document);
      // closing the modal
      CMSEditor.util.attachClick(
        document,
        '.sling-cms-editor .modal-close, .sling-cms-editor .modal-background',
        () => {
          CMSEditor.ui.hideModal();
        },
      );
      window.addEventListener('keypress', (e) => {
        if (e.keyCode === 27 && CMSEditor.ui.modalDisplayed === true) {
          CMSEditor.ui.hideModal();
        }
      });
    },
    ui: {
      draggable(element) {
        CMSEditor.draggable = element;
        let mouseDown = false;
        let elementX = 0;
        let elementY = 0;
        function moveComplete() {
          mouseDown = false;
          elementX = parseInt(CMSEditor.draggable.style.left, 10) || 0;
          elementY = parseInt(CMSEditor.draggable.style.top, 10) || 0;
          return false;
        }

        // mouse button down over the element
        CMSEditor.draggable.addEventListener('mousedown', (evt) => {
          if (!evt.target.matches('.modal-title, .modal-title *')) {
            return;
          }
          mouseX = evt.clientX;
          mouseY = evt.clientY;
          mouseDown = true;
        });
        CMSEditor.draggable.addEventListener('mouseup', moveComplete);
        document.addEventListener('mouseout', moveComplete);
        document.addEventListener('mousemove', (event) => {
          if (!mouseDown) {
            return;
          }
          const deltaX = event.clientX - mouseX;
          const deltaY = event.clientY - mouseY;
          CMSEditor.draggable.style.left = `${elementX + deltaX}px`;
          CMSEditor.draggable.style.top = `${elementY + deltaY}px`;
        });
      },
      modalDisplayed: false,
      hideModal() {
        if (CMSEditor.ui.modalDisplayed) {
          if (document.querySelector('.sling-cms-editor .modal')) {
            document.querySelector('.sling-cms-editor .modal').remove();
          }
          CMSEditor.ui.modalDisplayed = false;
        }
      },
      reloadComponent(ip, cb) {
        let path = ip;
        let component = document.querySelector(
          `.sling-cms-component[data-sling-cms-resource-path="${path}"]`,
        );
        let forceReload = component && component.dataset.reload === 'true';
        while (!component && path.length > 1) {
          const pathArr = path.split('/');
          pathArr.pop();
          path = pathArr.join('/');
          component = document.querySelector(
            `.sling-cms-component[data-sling-cms-resource-path="${path}"]`,
          );
          if (component && component.dataset.reload === 'true') {
            forceReload = true;
          }
        }
        if (!component || forceReload) {
          CMSEditor.ui.hideModal();
          window.top.location.reload();
        }
        fetch(
          `/cms/page/pagewrapper.html${path}?forceResourceType=${component.dataset.slingCmsResourceType}`,
          {
            redirect: 'manual',
          },
        )
          .then((response) => {
            if (!response.ok) {
              CMSEditor.ui.hideModal();
              window.top.location.reload();
            }
            return response.text();
          })
          .then((html) => {
            const tmp = document.createElement('div');
            tmp.innerHTML = html;
            CMSEditor.util.attachEvents(tmp);
            component.replaceWith(tmp.querySelector('.sling-cms-component'));
            tmp.remove();
            CMSEditor.ui.hideModal();
            cb();
          });
      },
      showModal(url, t) {
        const title = t || '';
        if (CMSEditor.ui.modalDisplayed) {
          CMSEditor.ui.hideModal();
        }
        document.querySelector('.sling-cms-editor-final').innerHTML = '<div class="modal"><div class="modal-background"></div><div class="modal-content"><div class="box"><h3 class="modal-title"></h3><section class="modal-body"></section></div></div><button class="modal-close is-large" aria-label="close"></button>';
        document.querySelector(
          '.sling-cms-editor .modal-title',
        ).innerText = title;
        document.querySelector(
          '.sling-cms-editor .modal-body',
        ).innerHTML = `<iframe class="modal-frame" src="${url}"></iframe>`;
        document
          .querySelector('.sling-cms-editor .modal')
          .classList.add('is-active');
        CMSEditor.util.attachClick(
          document,
          '.sling-cms-editor .modal-background, .sling-cms-editor .modal-close',
          () => {
            CMSEditor.ui.hideModal();
          },
        );
        CMSEditor.ui.draggable(
          document.querySelector('.sling-cms-editor .modal-content'),
        );
        CMSEditor.ui.modalDisplayed = true;
      },
    },
    util: {
      attachClick(ctx, exp, cb) {
        ctx.querySelectorAll(exp).forEach((el) => {
          el.addEventListener('click', cb, false);
        });
      },
      attachDrag(ed) {
        let dragImage;
        let dragActive = false;
        function handleOver(evt) {
          evt.preventDefault();
          document.querySelectorAll('.sling-cms-droptarget').forEach((dt) => {
            dt.classList.remove('sling-cms-droptarget__is-over');
          });
          evt.target.classList.add('sling-cms-droptarget__is-over');
        }
        function handleLeave(evt) {
          evt.target.classList.remove('sling-cms-droptarget__is-over');
        }
        function handleDrop(evt) {
          evt.preventDefault();

          const sourcePath = evt.dataTransfer.getData('path');
          const sourceName = evt.dataTransfer.getData('name');
          const targetPath = `${evt.target.dataset.path}/${sourceName}`;

          async function move() {
            const formData = new FormData();
            formData.append('_charset_', 'utf-8');
            if (sourcePath !== targetPath) {
              formData.append(':dest', targetPath);
              formData.append(':operation', 'move');
            }
            formData.append(':nameHint', sourceName);
            formData.append(':order', evt.target.dataset.order);
            let ui;
            if (typeof Sling !== 'undefined') {
              ui = Sling.CMS.ui;
            } else {
              ui = window.parent.Sling.CMS.ui;
            }
            const response = await fetch(sourcePath, {
              method: 'POST',
              body: formData,
              cache: 'no-cache',
              headers: {
                Accept: 'application/json',
              },
            });
            const res = await response.json();
            if (!response.ok) {
              ui.confirmMessage(
                'Failed to move',
                res['status.message'] || response.statusText,
                () => {},
              );
            } else {
              ui.confirmReload(res, 'success');
            }
            evt.target.classList.remove('sling-cms-droptarget__is-over');
          }
          if (evt.target.dataset.create) {
            const formData = new FormData();
            formData.append('_charset_', 'utf-8');
            formData.append('jcr:primaryType', 'nt:unstructured');
            let ui;
            if (typeof Sling !== 'undefined') {
              ui = Sling.CMS.ui;
            } else {
              ui = window.parent.Sling.CMS.ui;
            }
            fetch(evt.target.dataset.path, {
              method: 'POST',
              body: formData,
              cache: 'no-cache',
              headers: {
                Accept: 'application/json',
              },
            })
              .then((response) => {
                if (!response.ok) {
                  throw new Error(response.statusText);
                }
                return response.json();
              })
              .catch((error) => {
                ui.confirmMessage(error.message, error.message, () => {});
              })
              .then(() => {
                move();
              });
          } else {
            move();
          }
        }
        function activateTargets() {
          if (dragActive) {
            document.querySelectorAll('.sling-cms-droptarget').forEach((dt) => {
              dt.classList.add('sling-cms-droptarget__is-active');
              dt.addEventListener('dragover', handleOver);
              dt.addEventListener('dragleave', handleLeave);
              dt.addEventListener('drop', handleDrop);
            });
          }
        }
        function deactivateTargets() {
          dragActive = false;
          document.querySelectorAll('.sling-cms-droptarget').forEach((dt) => {
            dt.classList.remove('sling-cms-droptarget__is-active');
            dt.removeEventListener('dragover', handleOver);
            dt.removeEventListener('dragleave', handleLeave);
            dt.removeEventListener('drop', handleDrop);
          });
          if (dragImage) {
            dragImage.remove();
          }
        }
        ed.addEventListener('dragstart', (evt) => {
          const event = evt;
          event.stopPropagation();
          dragActive = true;
          event.dataTransfer.effectAllowed = 'move';
          dragImage = document.createElement('div');
          dragImage.setAttribute(
            'style',
            `position: absolute; left: 0px; top: 0px; width: ${ed.offsetWidth}px; height: ${ed.offsetHeight}px; padding: 0.5em; background: grey; z-index: -1`,
          );
          dragImage.innerText = ed.querySelector('.level-right').innerText;
          document.body.appendChild(dragImage);
          event.dataTransfer.setDragImage(dragImage, 20, 20);
          event.dataTransfer.setData(
            'path',
            ed.closest('.sling-cms-component').dataset.slingCmsResourcePath,
          );
          const resourceType = ed.closest('.sling-cms-component').dataset.slingCmsResourceType;
          event.dataTransfer.setData(
            'name',
            resourceType.substr(resourceType.lastIndexOf('/') + 1) + '_' + Math.random().toString(36).substring(2)
          );
          
          setTimeout(activateTargets, 10);
        });
        ed.addEventListener('dragend', deactivateTargets);
      },
      attachEvents(ctx) {
        function handleClick(event) {
          event.preventDefault();
          CMSEditor.ui.showModal(this.href, this.title);
        }
        CMSEditor.util.attachClick(ctx, '.sling-cms-editor .action-button', handleClick);
        if (ctx.matches && ctx.matches('.sling-cms-component')) {
          CMSEditor.util.attachHover(ctx);
        }
        ctx.querySelectorAll('.sling-cms-component').forEach((c) => {
          CMSEditor.util.attachHover(c);
        });
        ctx
          .querySelectorAll('.sling-cms-editor')
          .forEach(CMSEditor.util.attachDrag);
      },
      attachHover(ctx) {
        function resetActive() {
          document
            .querySelectorAll('.sling-cms-component__is-active')
            .forEach((a) => {
              a.classList.remove('sling-cms-component__is-active');
            });
        }
        ctx.addEventListener('mouseover', (evt) => {
          resetActive();
          ctx.classList.add('sling-cms-component__is-active');
          evt.stopPropagation();
        });
        ctx.addEventListener('mouseleave', resetActive);
      },
      findParent(el, exp) {
        if (el === null || el.parentElement === null) {
          return null;
        }
        if (el.parentElement.matches(exp)) {
          return el.parentElement;
        }
        return CMSEditor.util.findParent(el.parentElement, exp);
      },
    },
  };
  window.CMSEditor = CMSEditor;
  window.onbeforeunload = () => {
    if (CMSEditor.ui.modalDisplayed) {
      return 'Are you sure you want to leave this page?';
    }
    return null;
  };
  if (document.readyState === 'complete') {
    window.CMSEditor.init();
  } else {
    document.addEventListener('DOMContentLoaded', CMSEditor.init, false);
  }
}
